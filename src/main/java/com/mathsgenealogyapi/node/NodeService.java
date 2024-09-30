package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.Constants;
import com.mathsgenealogyapi.NodeDoesNotExistException;
import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.edge.Edge;
import com.mathsgenealogyapi.scraper.ScrapedDissertationData;
import com.mathsgenealogyapi.scraper.ScrapedNodeData;
import com.mathsgenealogyapi.scraper.ScrapedStudentData;
import com.mathsgenealogyapi.scraper.Scraper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    NodeRepository repository;
    @Autowired
    ConversionService conversionService;

    private static final Logger logger = LogManager.getLogger(NodeService.class);


    public Node test(Integer id) throws IOException, NodeDoesNotExistException {
        return scrapeNode(id);
    }


    // Turns ScrapedNodeData into a collection of Node, Edge, and Dissertation to be persisted to the database
    private Node scrapeNode(Integer id) throws IOException, NodeDoesNotExistException {
        ScrapedNodeData scrapedNode = Scraper.scrapeNode(id);

        Node node = new Node(id);

        node.setName(scrapedNode.name());
        node.setNumberofdescendents(scrapedNode.numberofdescendents());
        node.setLastupdated(LocalDateTime.now());
        node.setScraped(true);

        List<Dissertation> dissertations = new ArrayList<Dissertation>();
        node.setDissertations(dissertations);

        List<Edge> advisorEdges = new ArrayList<Edge>();
        node.setAdvisorEdges(advisorEdges);

        for (ScrapedDissertationData dissertationData : scrapedNode.dissertations()) {
            Node advisor1Node = getOrCreateNode(dissertationData.advisor1id(), dissertationData.advisor1name());
            logger.debug(advisor1Node.toString());
            Node advisor2Node = getOrCreateNode(dissertationData.advisor2id(), dissertationData.advisor2name());
            logger.debug(advisor2Node.toString());

            Edge advisor1Edge = new Edge(advisor1Node, node);
            Edge advisor2Edge = new Edge(advisor2Node, node);
            logger.debug("Advisor 1 edge: " + advisor1Edge.getFromNode().getId());

            Dissertation dissertation = new Dissertation();
            dissertation.setNode(node);
            dissertation.setPhdprefix(dissertationData.phdprefix());
            dissertation.setUniversity(dissertationData.university());
            dissertation.setYearofcompletion(dissertationData.yearofcompletion());
            dissertation.setDissertationtitle(dissertationData.dissertationtitle());
            dissertation.setMscnumber(dissertationData.mscnumber());

            dissertation.setAdvisor1_id(dissertationData.advisor1id());
            dissertation.setAdvisor2_id(dissertationData.advisor2id());

            dissertation.setAdvisor1edge(advisor1Edge);
            dissertation.setAdvisor2edge(advisor2Edge);

            advisorEdges.add(advisor1Edge);
            logger.debug("Advisor 1 edge after adding:" + dissertation.getAdvisor1edge().getFromNode().getId());
            advisorEdges.add(advisor2Edge);

            dissertations.add(dissertation);
        }

        List<Edge> studentEdges = new ArrayList<Edge>();
        node.setStudentEdges(studentEdges);

        for (ScrapedStudentData studentData : scrapedNode.students()) {
            Node studentNode = getOrCreateNode(studentData.student().id(), studentData.student().name());
            Edge studentEdge = new Edge(node, studentNode);
            studentEdges.add(studentEdge);
        }

        return node;
    }

    private Node getOrCreateNode(Integer id, String name) {
        logger.info("Getting or inserting node " + id);
        return repository.getOrInsert(new Node(id, name));
    }

    private Node addOrUpdateNode(Node newNode) {
        logger.info("Adding or updating node " + newNode.getId());
        return repository.save(newNode);
    }

    public NodeDto getSingleNode(Integer id) throws NodeDoesNotExistException, IOException {
        Optional<Node> requested = repository.findById(id);

        if (requested.isEmpty() || !requested.get().getScraped() || requested.get().getLastupdated().isBefore(LocalDateTime.now().minusDays(Constants.daysToInvalidateCache))) {
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
