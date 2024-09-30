package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.util.MathsgenealogyapiPostgresqlContainer;
import org.junit.ClassRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;


@SpringBootTest
class NodeRepositoryTest {

    @ClassRule
    public static PostgreSQLContainer<MathsgenealogyapiPostgresqlContainer> postgreSQLContainer = MathsgenealogyapiPostgresqlContainer.getInstance();

    @Autowired
    NodeRepository repository;


}