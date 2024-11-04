package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.util.MathsgenealogyapiPostgresqlContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@ActiveProfiles("medium")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class NodeRepositoryTest {

    private static final Logger logger = LogManager.getLogger(NodeRepositoryTest.class);

    @Container
    public static PostgreSQLContainer<MathsgenealogyapiPostgresqlContainer> postgreSQLContainer = MathsgenealogyapiPostgresqlContainer.getInstance();
            //.withInitScript("database/testData.sql");

    @Autowired
    NodeRepository repository;

    @Test
    @Transactional
    public void postgres1() {
        logger.info("Running john test");

        Node testNodeJohn = new Node();
        testNodeJohn.setId(293462);
        testNodeJohn.setName("John Benjamin McCarthy");
        testNodeJohn.setScraped(false);
        testNodeJohn.setLastupdated(LocalDateTime.now());
        repository.save(testNodeJohn);

        Optional<Node> foundNode = repository.findById(293462);
        assertTrue(foundNode.isPresent());
        assertEquals("John Benjamin McCarthy", foundNode.get().getName());
    }

    @Test
    @Transactional
    public void postgres2() {
        logger.info("Running ruadhai test");

        Node testNodeRuadhai = new Node();
        testNodeRuadhai.setId(217413);
        testNodeRuadhai.setName("Ruadhaí Dervan");
        testNodeRuadhai.setScraped(false);
        testNodeRuadhai.setLastupdated(LocalDateTime.now());
        repository.save(testNodeRuadhai);

        Optional<Node> foundNode = repository.findById(217413);
        assertTrue(foundNode.isPresent());
        assertEquals("Ruadhaí Dervan", foundNode.get().getName());
    }

    @Test
    @Transactional
    public void datatest() {
        logger.info("Running Julius Test");

        Optional<Node> foundNode = repository.findById(93925);
        assertTrue(foundNode.isPresent());
    }



    @Test
    @Transactional
    public void graphTest2() {
        List<Pair<Node, Integer>> nodes = repository.getNodes(293462, 1,1);
        for (Pair<Node, Integer> nodeWithGenerationsCount: nodes) {
            Node node = nodeWithGenerationsCount.getFirst();
            Integer generationsCount = nodeWithGenerationsCount.getSecond();
            logger.info(node.getName() + " " + generationsCount);
        }
        assertTrue(true);
    }




}