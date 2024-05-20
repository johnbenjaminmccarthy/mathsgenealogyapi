package com.example.mathsgenealogyapi.entry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EntryControllerTest {

    @Autowired
    private EntryController controller;

    @Test
    void scrapeTestJohn() throws IOException , EntryDoesNotExistException {
        Entry john = controller.test(293462L);

        assertEquals("John Benjamin McCarthy",john.getName());
        assertEquals(293462L,john.getId());
        assertEquals("Stability conditions and canonical metrics", john.getDissertationtitle());
        assertEquals("Ph.D.",john.getPhdprefix());
        assertEquals("Imperial College London",john.getUniversity());
        assertEquals(2023,john.getYearofcompletion());
        assertEquals("53",john.getMscnumber());
        assertEquals(36909L,john.getAdvisor1id());
        assertEquals("Simon Kirwan Donaldson",john.getAdvisor1name());
        assertEquals(217413L,john.getAdvisor2id());
        assertEquals("Ruadhaí Dervan",john.getAdvisor2name());
        assertEquals(0,john.getNumberofdescendents());
        assertTrue(john.getStudents().isEmpty());
    }

    @Test
    void scrapeTestKelli() throws IOException , EntryDoesNotExistException {
        Entry kelli = controller.test(282363L);
        assertEquals("Dominic Joyce", kelli.getAdvisor1name());
        assertEquals("", kelli.getAdvisor2name());
    }

    @Test
    void scrapeTestRuadhai() throws IOException , EntryDoesNotExistException {
        Entry ruadhai = controller.test(217413L);
        assertEquals("Ruadhaí Dervan", ruadhai.getName());
        assertEquals(3, ruadhai.getNumberofdescendents());
    }

    @Test
    void scrapeTestalMasihi() throws EntryDoesNotExistException, IOException {
        Entry alMasihi = controller.test(310782L); //Fails because there is unknown year of completion!
        assertEquals("", alMasihi.getAdvisor1name());
    }

    @Test
    void scrapeTestSimon() throws IOException , EntryDoesNotExistException {
        Entry simon = controller.test(36909L);
    }
}