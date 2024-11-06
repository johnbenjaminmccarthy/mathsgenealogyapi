package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.util.MathsgenealogyapiPostgresqlContainer;
import com.mathsgenealogyapi.util.TestData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.processing.SQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@DataJpaTest
//@SpringBootTest
@ActiveProfiles("medium")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class NodeRepositoryTest {

    private static final Logger logger = LogManager.getLogger(NodeRepositoryTest.class);

    @Container
    public static PostgreSQLContainer<MathsgenealogyapiPostgresqlContainer> postgreSQLContainer = MathsgenealogyapiPostgresqlContainer.getInstance();

    @Autowired
    NodeRepository repository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    @Transactional
    public void zeroGenerationsUpOrDown() {
        int[][][] data = {{{1,1,1},{2,1,1},{3,1,1}},
                {{1,2},{2,3}}};
        String sql = TestData.fromArray(data).bulkInsertSQL();

        testEntityManager.getEntityManager().createNativeQuery(sql).executeUpdate();
        List<Pair<Node, Integer>> output = repository.getNodes(2, 1, 1);
        assertEquals(3, output.size());
    }

    @Test
    @Transactional
    public void zeroGenerationsUpOrDown2() {
        int[][][] data = {{{1,1,1},{2,1,1},{3,1,1},{4,1,1}},
                {{1,2},{2,3},{3,4}}};
        String sql = TestData.fromArray(data).bulkInsertSQL();

        testEntityManager.getEntityManager().createNativeQuery(sql).executeUpdate();
        List<Pair<Node, Integer>> output = repository.getNodes(2, 1, 1);
        assertEquals(3, output.size());
    }

}

