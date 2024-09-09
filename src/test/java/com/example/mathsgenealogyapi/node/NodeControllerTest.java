package com.example.mathsgenealogyapi.node;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NodeControllerTest {
    @Autowired
    private NodeController controller;


}