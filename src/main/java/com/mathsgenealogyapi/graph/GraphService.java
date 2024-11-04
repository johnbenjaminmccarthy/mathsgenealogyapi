package com.mathsgenealogyapi.graph;

import com.mathsgenealogyapi.NodeDoesNotExistException;
import com.mathsgenealogyapi.dissertation.DissertationRepository;
import com.mathsgenealogyapi.edge.Edge;
import com.mathsgenealogyapi.node.Node;
import com.mathsgenealogyapi.node.NodeRepository;
import com.mathsgenealogyapi.node.NodeService;
import com.mathsgenealogyapi.scraper.ScrapedNodeData;
import com.mathsgenealogyapi.scraper.Scraper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GraphService {

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    NodeService nodeService;

    @Autowired
    ConversionService conversionService;

    private static final Logger logger = LogManager.getLogger(GraphService.class);


    private Graph nodeListToGraph(List<Node> nodes, Integer baseId) {
        Set<Edge> edges = new HashSet<>();
        for (Node node: nodes) {
            edges.addAll(node.getAdvisorEdges());
            edges.addAll(node.getStudentEdges());
        }
        return new Graph(baseId, nodes, edges.stream().toList());
    }

    private List<Node> scrapeUnscrapedAdvisors(Node currentNode, Integer currentGeneration, Integer maxGenerationsUp, Set<Integer> alreadyFoundNodes) throws NodeDoesNotExistException, IOException {
        logger.info("Scraping unscraped advisors of " + currentNode.getName() + " with id " + currentNode.getId() + ". Generation = " + currentGeneration);
        if (currentGeneration >= 0 && currentGeneration < maxGenerationsUp) {
            List<Node> advisorNodes = new ArrayList<>();
            for (Edge advisorEdge : currentNode.getAdvisorEdges()) {
                logger.info("Checking advisor " + advisorEdge.getFromNode().getName() + " with id " + advisorEdge.getFromNode().getId() + ".");
                if (!alreadyFoundNodes.contains(advisorEdge.getFromNode().getId())) {
                    logger.info("Advisor " + advisorEdge.getFromNode().getName() + " was not already in list. Scraping...");
                    if (NodeService.needsToBeScraped(advisorEdge.getFromNode())) {
                        Node scrapedNode = nodeService.scrapeNode(advisorEdge.getFromNode().getId());

                        nodeService.addOrUpdateNode(scrapedNode);
                        advisorNodes.add(scrapedNode);
                        alreadyFoundNodes.add(scrapedNode.getId());
                        advisorNodes.addAll(scrapeUnscrapedAdvisors(scrapedNode, currentGeneration + 1, maxGenerationsUp, alreadyFoundNodes));
                    }
                }
            }
            return advisorNodes;
        }
        else {
            return new ArrayList<>();
        }
    }

    private List<Node> scrapeUnscrapedStudents(Node currentNode, Integer currentGeneration, Integer maxGenerationsDown, Set<Integer> alreadyFoundNodes) throws NodeDoesNotExistException, IOException {
        if (currentGeneration <= 0 && -currentGeneration < maxGenerationsDown) {
            List<Node> studentNodes = new ArrayList<>();
            for (Edge studentEdge : currentNode.getStudentEdges()) {
                if (!alreadyFoundNodes.contains(studentEdge.getToNode().getId())) {
                    if (NodeService.needsToBeScraped(studentEdge.getToNode())) {
                        Node scrapedNode = nodeService.scrapeNode(studentEdge.getToNode().getId());

                        nodeService.addOrUpdateNode(scrapedNode);
                        studentNodes.add(scrapedNode);
                        alreadyFoundNodes.add(scrapedNode.getId());
                        studentNodes.addAll(scrapeUnscrapedStudents(scrapedNode, currentGeneration - 1, maxGenerationsDown, alreadyFoundNodes));
                    }
                }
            }
            return studentNodes;
        }
        else {
            return new ArrayList<>();
        }
    }

    public GraphDto getGraph(Integer id, Integer maxGenerationsUp, Integer maxGenerationsDown) throws NodeDoesNotExistException, IOException {
        List<Pair<Node, Integer>> nodesWithGenerations = nodeRepository.getNodes(id, maxGenerationsDown, maxGenerationsUp);
        if (nodesWithGenerations.isEmpty()) {
            logger.info("Empty database return for request with base id " + id + ". Starting scraping at this id.");
            Node baseNode = new Node(id);
            nodesWithGenerations.add(Pair.of(baseNode, 0));
        }

        Set<Integer> upToDateNodesInList = new HashSet<>(nodesWithGenerations.stream().filter(it -> !NodeService.needsToBeScraped(it.getFirst())).map(it -> it.getFirst().getId()).toList());
        List<Node> nodes = new ArrayList<>();

        for (Pair<Node, Integer> nodeWithGeneration : nodesWithGenerations) {
            if (NodeService.needsToBeScraped(nodeWithGeneration.getFirst())) {
                logger.info("Node (" + nodeWithGeneration.getFirst().getId() + ")" + nodeWithGeneration.getFirst().getName() + " was returned from database but needs to be scraped.");
                Integer generation = nodeWithGeneration.getSecond();
                Node scrapedNode = nodeService.scrapeNode(nodeWithGeneration.getFirst().getId());
                nodeService.addOrUpdateNode(scrapedNode);
                nodes.add(scrapedNode);
                upToDateNodesInList.add(scrapedNode.getId());

                nodes.addAll(scrapeUnscrapedAdvisors(scrapedNode, generation, maxGenerationsUp, upToDateNodesInList));
                nodes.addAll(scrapeUnscrapedStudents(scrapedNode, generation, maxGenerationsDown, upToDateNodesInList));
            }
            else {
                nodes.add(nodeWithGeneration.getFirst());
            }
        }

        Graph newGraph = nodeListToGraph(nodes, id);
        return conversionService.convert(newGraph, GraphDto.class);

    }
}
