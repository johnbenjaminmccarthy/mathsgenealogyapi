package com.example.mathsgenealogyapi.node;

import com.example.mathsgenealogyapi.util.MathsgenealogyapiPostgresqlContainer;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class NodeRepositoryTest {

    @ClassRule
    public static PostgreSQLContainer<MathsgenealogyapiPostgresqlContainer> postgreSQLContainer = MathsgenealogyapiPostgresqlContainer.getInstance();

    @Autowired NodeRepository repository;


}