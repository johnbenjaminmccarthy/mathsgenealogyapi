package com.example.mathsgenealogyapi.node;

import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NodeServiceTest {

    @Autowired private NodeService service;

    @Test
    void scrapeTestJohn() throws IOException , NodeDoesNotExistException {
        Node john = service.test(293462);

        assertEquals("John Benjamin McCarthy",john.getName());
        assertEquals(293462,john.getGenealogyId());
        assertEquals("Stability conditions and canonical metrics", john.getDissertations().get(0).getDissertationtitle());
        assertEquals("Ph.D.",john.getDissertations().get(0).getPhdprefix());
        assertEquals("Imperial College London",john.getDissertations().get(0).getUniversity());
        assertEquals(2023,john.getDissertations().get(0).getYearofcompletion());
        assertEquals("53",john.getDissertations().get(0).getMscnumber());
        assertEquals(36909,john.getDissertations().get(0).getAdvisor1id());
        assertEquals("Simon Kirwan Donaldson",john.getDissertations().get(0).getAdvisor1name());
        assertEquals(217413,john.getDissertations().get(0).getAdvisor2id());
        assertEquals("Ruadhaí Dervan",john.getDissertations().get(0).getAdvisor2name());
        assertEquals(0,john.getNumberofdescendents());
        assertTrue(john.getStudents().isEmpty());
    }

    @Test
    void scrapeTestKelli() throws IOException , NodeDoesNotExistException {
        Node kelli = service.test(282363);
        assertEquals("Dominic Joyce", kelli.getDissertations().get(0).getAdvisor1name());
        assertEquals("", kelli.getDissertations().get(0).getAdvisor2name());
    }

    @Test
    void scrapeTestRuadhai() throws IOException , NodeDoesNotExistException {
        Node ruadhai = service.test(217413);
        assertEquals("Ruadhaí Dervan", ruadhai.getName());
        assertEquals(3, ruadhai.getNumberofdescendents());
    }

    @Test
    void scrapeTestalMasihi() throws NodeDoesNotExistException, IOException {
        Node alMasihi = service.test(310782); //Fails because there is unknown year of completion!
        assertEquals("", alMasihi.getDissertations().get(0).getAdvisor1name());
    }

    @Test
    void scrapeTestSimon() throws IOException , NodeDoesNotExistException {
        Node simon = service.test(36909);
    }

}