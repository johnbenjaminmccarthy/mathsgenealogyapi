package com.mathsgenealogyapi.graph;

import com.mathsgenealogyapi.NodeDoesNotExistException;
import com.mathsgenealogyapi.edge.Edge;
import com.mathsgenealogyapi.node.Node;
import com.mathsgenealogyapi.node.NodeRepository;
import com.mathsgenealogyapi.node.NodeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GraphService {

    final NodeRepository nodeRepository;

    final NodeService nodeService;

    final ConversionService conversionService;

    private static final Logger logger = LogManager.getLogger(GraphService.class);

    public GraphService(NodeRepository nodeRepository, NodeService nodeService, ConversionService conversionService) {
        this.nodeRepository = nodeRepository;
        this.nodeService = nodeService;
        this.conversionService = conversionService;
    }


    private Graph nodeListToGraph(List<Pair<Node, Integer>> nodesWithGeneration, Integer baseId, Integer maximumReachedGeneration, Integer minimumReachedGeneration) {
        Set<Edge> edges = new HashSet<>();
        Set<Node> nodes = new HashSet<>();
        for (Pair<Node, Integer> node: nodesWithGeneration) {
            Integer generation = node.getSecond();
            if (generation >= 0) {
                edges.addAll(node.getFirst().getAdvisorEdges());
            }
            if (generation <= 0) {
                edges.addAll(node.getFirst().getStudentEdges());
            }
            nodes.add(node.getFirst());
        }
        return new Graph(baseId, nodes.stream().toList(), edges.stream().toList(), maximumReachedGeneration, minimumReachedGeneration);
    }

    private Pair<List<Pair<Node, Integer>>, Integer> scrapeUnscrapedAdvisors(Node currentNode, Integer currentGeneration, Integer maxGenerationsUp, Set<Integer> alreadyFoundNodes, Integer maximumReachedGeneration) throws NodeDoesNotExistException, IOException {
        logger.info("Scraping unscraped advisors of " + currentNode.getName() + " with id " + currentNode.getId() + ". Generation = " + currentGeneration);
        if (currentGeneration >= 0 && currentGeneration < maxGenerationsUp) {
            List<Pair<Node, Integer>> advisorNodes = new ArrayList<>();
            for (Edge advisorEdge : currentNode.getAdvisorEdges()) {
                logger.info("Checking advisor " + advisorEdge.getFromNode().getName() + " with id " + advisorEdge.getFromNode().getId() + ".");
                if (!alreadyFoundNodes.contains(advisorEdge.getFromNode().getId())) {
                    logger.info("Advisor " + advisorEdge.getFromNode().getName() + " was not already in list. Scraping...");
                    if (NodeService.needsToBeScraped(advisorEdge.getFromNode())) {
                        Node scrapedNode = nodeService.scrapeNode(advisorEdge.getFromNode().getId());

                        nodeService.addOrUpdateNode(scrapedNode);
                        advisorNodes.add(Pair.of(scrapedNode, currentGeneration + 1));
                        alreadyFoundNodes.add(scrapedNode.getId());

                        maximumReachedGeneration = Integer.max(maximumReachedGeneration, currentGeneration + 1);

                        advisorNodes.addAll(scrapeUnscrapedAdvisors(scrapedNode, currentGeneration + 1, maxGenerationsUp, alreadyFoundNodes, maximumReachedGeneration).getFirst());
                    }
                }
            }
            return Pair.of(advisorNodes, maximumReachedGeneration);
        }
        else {
            return Pair.of(new ArrayList<>(), maximumReachedGeneration);
        }
    }

    private Pair<List<Pair<Node, Integer>>, Integer> scrapeUnscrapedStudents(Node currentNode, Integer currentGeneration, Integer maxGenerationsDown, Set<Integer> alreadyFoundNodes, Integer minimumReachedGeneration) throws NodeDoesNotExistException, IOException {
        if (currentGeneration <= 0 && -currentGeneration < maxGenerationsDown) {
            List<Pair<Node, Integer>> studentNodes = new ArrayList<>();
            for (Edge studentEdge : currentNode.getStudentEdges()) {
                if (!alreadyFoundNodes.contains(studentEdge.getToNode().getId())) {
                    if (NodeService.needsToBeScraped(studentEdge.getToNode())) {
                        Node scrapedNode = nodeService.scrapeNode(studentEdge.getToNode().getId());

                        nodeService.addOrUpdateNode(scrapedNode);
                        studentNodes.add(Pair.of(scrapedNode, currentGeneration - 1));
                        alreadyFoundNodes.add(scrapedNode.getId());
                        minimumReachedGeneration = Integer.min(minimumReachedGeneration, currentGeneration - 1);
                        studentNodes.addAll(scrapeUnscrapedStudents(scrapedNode, currentGeneration - 1, maxGenerationsDown, alreadyFoundNodes, minimumReachedGeneration).getFirst());
                    }
                }
            }
            return Pair.of(studentNodes, minimumReachedGeneration);
        }
        else {
            return Pair.of(new ArrayList<>(), minimumReachedGeneration);
        }
    }

    public GraphDto getGraph(Integer id, Integer maxGenerationsUp, Integer maxGenerationsDown) throws NodeDoesNotExistException, IOException {
        List<Pair<Node, Integer>> nodesWithGenerationsFromDatabase = nodeRepository.getNodes(id, maxGenerationsDown, maxGenerationsUp);
        if (nodesWithGenerationsFromDatabase.isEmpty()) {
            logger.info("Empty database return for request with base id " + id + ". Starting scraping at this id.");
            Node baseNode = new Node(id);
            nodesWithGenerationsFromDatabase.add(Pair.of(baseNode, 0));
        }

        Set<Integer> upToDateNodesInList = new HashSet<>(nodesWithGenerationsFromDatabase.stream().filter(it -> !NodeService.needsToBeScraped(it.getFirst())).map(it -> it.getFirst().getId()).toList());
        List<Pair<Node, Integer>> nodesWithGeneration = new ArrayList<>();

        Integer maximumReachedGeneration = 0;
        Integer minimumReachedGeneration = 0;

        for (Pair<Node, Integer> nodeWithGeneration : nodesWithGenerationsFromDatabase) {
            if (NodeService.needsToBeScraped(nodeWithGeneration.getFirst())) {
                logger.info("Node (" + nodeWithGeneration.getFirst().getId() + ")" + nodeWithGeneration.getFirst().getName() + " was returned from database but needs to be scraped.");
                Integer generation = nodeWithGeneration.getSecond();
                Node scrapedNode = nodeService.scrapeNode(nodeWithGeneration.getFirst().getId());
                nodeService.addOrUpdateNode(scrapedNode);
                nodesWithGeneration.add(Pair.of(scrapedNode, generation));
                upToDateNodesInList.add(scrapedNode.getId());

                Pair<List<Pair<Node, Integer>>,Integer> advisors = scrapeUnscrapedAdvisors(scrapedNode, generation, maxGenerationsUp, upToDateNodesInList, maximumReachedGeneration);
                maximumReachedGeneration = Integer.max(maximumReachedGeneration, advisors.getSecond());
                nodesWithGeneration.addAll(advisors.getFirst());

                Pair<List<Pair<Node, Integer>>, Integer> students = scrapeUnscrapedStudents(scrapedNode, generation, maxGenerationsDown, upToDateNodesInList, minimumReachedGeneration);
                minimumReachedGeneration = Integer.min(minimumReachedGeneration, students.getSecond());
                nodesWithGeneration.addAll(students.getFirst());



            }
            else {
                maximumReachedGeneration = Integer.max(maximumReachedGeneration, nodeWithGeneration.getSecond());
                minimumReachedGeneration = Integer.min(minimumReachedGeneration, nodeWithGeneration.getSecond());
                nodesWithGeneration.add(Pair.of(nodeWithGeneration.getFirst(), nodeWithGeneration.getSecond()));
            }
        }

        Graph newGraph = nodeListToGraph(nodesWithGeneration, id, maximumReachedGeneration, minimumReachedGeneration);
        return conversionService.convert(newGraph, GraphDto.class);

    }
}
