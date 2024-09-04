package com.example.mathsgenealogyapi.node;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NodeControllerTest {

    @Autowired
    private NodeController controller;

    @Test
    void scrapeTestJohn() throws IOException , NodeDoesNotExistException {
        Node john = controller.test(293462L);

        assertEquals("John Benjamin McCarthy",john.getName());
        assertEquals(293462L,john.getGenealogyId());
        assertEquals("Stability conditions and canonical metrics", john.getDissertations().get(0).getDissertationtitle());
        assertEquals("Ph.D.",john.getDissertations().get(0).getPhdprefix());
        assertEquals("Imperial College London",john.getDissertations().get(0).getUniversity());
        assertEquals(2023,john.getDissertations().get(0).getYearofcompletion());
        assertEquals("53",john.getDissertations().get(0).getMscnumber());
        assertEquals(36909L,john.getDissertations().get(0).getAdvisor1id());
        assertEquals("Simon Kirwan Donaldson",john.getDissertations().get(0).getAdvisor1name());
        assertEquals(217413L,john.getDissertations().get(0).getAdvisor2id());
        assertEquals("Ruadhaí Dervan",john.getDissertations().get(0).getAdvisor2name());
        assertEquals(0,john.getNumberofdescendents());
        assertTrue(john.getStudents().isEmpty());
    }

    @Test
    void scrapeTestKelli() throws IOException , NodeDoesNotExistException {
        Node kelli = controller.test(282363L);
        assertEquals("Dominic Joyce", kelli.getDissertations().get(0).getAdvisor1name());
        assertEquals("", kelli.getDissertations().get(0).getAdvisor2name());
    }

    @Test
    void scrapeTestRuadhai() throws IOException , NodeDoesNotExistException {
        Node ruadhai = controller.test(217413L);
        assertEquals("Ruadhaí Dervan", ruadhai.getName());
        assertEquals(3, ruadhai.getNumberofdescendents());
    }

    @Test
    void scrapeTestalMasihi() throws NodeDoesNotExistException, IOException {
        Node alMasihi = controller.test(310782L); //Fails because there is unknown year of completion!
        assertEquals("", alMasihi.getDissertations().get(0).getAdvisor1name());
    }

    @Test
    void scrapeTestSimon() throws IOException , NodeDoesNotExistException {
        Node simon = controller.test(36909L);
    }
}