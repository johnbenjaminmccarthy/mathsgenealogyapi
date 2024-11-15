package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.Constants;
import com.mathsgenealogyapi.NodeDoesNotExistException;
import com.mathsgenealogyapi.advisor.Advisor;
import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.dissertation.DissertationRepository;
import com.mathsgenealogyapi.edge.Edge;
import com.mathsgenealogyapi.scraper.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class NodeService {
    final NodeRepository nodeRepository;
    final DissertationRepository dissertationRepository;
    final ConversionService conversionService;
    final Scraper scraper;

    private static final Logger logger = LogManager.getLogger(NodeService.class);

    public NodeService(NodeRepository nodeRepository, DissertationRepository dissertationRepository, ConversionService conversionService, Scraper scraper) {
        this.nodeRepository = nodeRepository;
        this.dissertationRepository = dissertationRepository;
        this.conversionService = conversionService;
        this.scraper = scraper;
    }


    // Turns ScrapedNodeData into a collection of Node, Edge, and Dissertation to be persisted to the database
    public Node scrapeNode(Integer id) throws IOException, NodeDoesNotExistException {
        ScrapedNodeData scrapedNode = scraper.scrapeNode(id);

        Node node = new Node(id);

        node.setName(scrapedNode.name());
        node.setNumberofdescendents(scrapedNode.numberofdescendents());
        node.setLastupdated(LocalDateTime.now());
        node.setScraped(true);

        List<Dissertation> dissertations = new ArrayList<>();
        node.setDissertations(dissertations);

        List<Edge> advisorEdges = new ArrayList<>();
        node.setAdvisorEdges(advisorEdges);

        for (ScrapedDissertationData dissertationData : scrapedNode.dissertations()) {
            Dissertation dissertation = new Dissertation();
            dissertation.setNode(node);

            List<Advisor> advisors = new ArrayList<>();
            dissertation.setAdvisors(advisors);

            for (ScrapedAdvisorData advisorData : dissertationData.advisors()) {
                Advisor advisor = new Advisor();
                Node advisorNode = getOrCreateNode(advisorData.advisorId(), advisorData.name());

                Edge advisorEdge = advisorEdges.stream().filter(it -> it.getFromNode().getId().equals(advisorNode.getId()) && it.getToNode().getId().equals(node.getId())).findFirst().orElse(new Edge(advisorNode, node));


                advisor.setDissertation(dissertation);
                advisor.setAdvisorId(advisorData.advisorId());
                advisor.setName(advisorData.name());
                advisor.setAdvisorNumber(advisorData.advisorNumber());

                if (advisorEdges.stream().noneMatch(it -> it.equals(advisorEdge))) {
                    advisorEdges.add(advisorEdge);
                }

                advisors.add(advisor);
            }

            dissertation.setPhdprefix(dissertationData.phdprefix());
            dissertation.setUniversity(dissertationData.university());
            dissertation.setYearofcompletion(dissertationData.yearofcompletion());
            dissertation.setDissertationtitle(dissertationData.dissertationtitle());
            dissertation.setMscnumber(dissertationData.mscnumber());


            dissertations.add(dissertation);
        }


        List<Edge> studentEdges = new ArrayList<>();
        node.setStudentEdges(studentEdges);

        for (ScrapedStudentData studentData : scrapedNode.students()) {
            Node studentNode = getOrCreateNode(studentData.student().id(), studentData.student().name());
            Edge studentEdge = studentEdges.stream().filter(it -> it.getFromNode().getId().equals(node.getId()) && it.getToNode().getId().equals(studentNode.getId())).findFirst().orElse(new Edge(node, studentNode));

            if (advisorEdges.stream().noneMatch(it -> it.equals(studentEdge))) {
                studentEdges.add(studentEdge);
            }

        }

        return node;
    }

    private Node getOrCreateNode(Integer id, String name) {
        return nodeRepository.getOrInsert(new Node(id, name));
    }

    public Node addOrUpdateNode(Node newNode) {

        dissertationRepository.deleteDissertationsWithNodeId(newNode.getId()); //Clean up old dissertations in the database for this node before potentially updating it
        return nodeRepository.saveAndLog(newNode, "addOrUpdate");
    }

    static public Boolean isOutOfDate(LocalDateTime lastUpdated) {
        return lastUpdated.isBefore(LocalDateTime.now().minusDays(Constants.daysToInvalidateCache));
    }

    static public Boolean needsToBeScraped(Node node) {
        return !node.getScraped() || isOutOfDate(node.getLastupdated());
    }

    public NodeDto getSingleNode(Integer id, Boolean forceupdate) throws NodeDoesNotExistException, IOException {
        Optional<Node> requested = nodeRepository.findById(id);

        if (forceupdate || requested.isEmpty() || NodeService.needsToBeScraped(requested.get())) {
            if (requested.isEmpty()) {
                logger.info("Node " + id + " was requested and didn't exist in database so was scraped");
            }
            else {
                logger.info("Node " + id + " was requested but was out of date or has not been scraped yet and was rescraped");
            }
            Node scrapedNode = scrapeNode(id);

            return conversionService.convert(addOrUpdateNode(scrapedNode), NodeDto.class);
        }
        else {
            logger.info("Node " + id + " was requested and returned without rescraping");
            return conversionService.convert(requested.get(), NodeDto.class);
        }
    }
}
