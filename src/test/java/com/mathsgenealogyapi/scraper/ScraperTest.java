package com.mathsgenealogyapi.scraper;

import com.mathsgenealogyapi.NodeDoesNotExistException;
import com.mathsgenealogyapi.node.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScraperTest {

    private static final Logger logger = LogManager.getLogger(ScraperTest.class);


    @Test
    void scrapeJohn_Scraping_should_return_name_and_advisors_and_one_dissertation_and_no_students() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData john = Scraper.scrapeNode(293462);

        assertEquals("John Benjamin McCarthy",john.name());
        assertEquals(293462,john.id());
        assertEquals(1, john.dissertations().size());
        assertEquals(john.id(), john.dissertations().get(0).studentId());
        assertEquals(john.name(), john.dissertations().get(0).studentName());
        assertEquals("Stability conditions and canonical metrics", john.dissertations().get(0).dissertationtitle());
        assertEquals("Ph.D.",john.dissertations().get(0).phdprefix());
        assertEquals("Imperial College London",john.dissertations().get(0).university());
        assertEquals(2023,john.dissertations().get(0).yearofcompletion());
        assertEquals("53",john.dissertations().get(0).mscnumber());
        assertEquals(36909,john.dissertations().get(0).advisor1id());
        assertEquals("Simon Kirwan Donaldson",john.dissertations().get(0).advisor1name());
        assertEquals(217413,john.dissertations().get(0).advisor2id());
        assertEquals("Ruadhaí Dervan",john.dissertations().get(0).advisor2name());
        assertEquals(0,john.numberofdescendents());
        assertTrue(john.students().isEmpty());
    }

    @Test
    void scrapeRuadhai_Scraping_should_return_name_with_special_character_and_at_least_3_descendents_and_have_null_second_advisor_name_and_id() throws IOException , NodeDoesNotExistException {
        ScrapedNodeData ruadhai = Scraper.scrapeNode(217413);
        assertEquals("Ruadhaí Dervan", ruadhai.name());
        assertTrue(ruadhai.numberofdescendents() >= 3);
        assertNull(ruadhai.dissertations().get(0).advisor2id());
        assertNull(ruadhai.dissertations().get(0).advisor2name());
    }

    @Test
    void scrapeAlMasihi_Scraping_should_have_dissertation_with_no_advisors_or_title() throws NodeDoesNotExistException, IOException {
        ScrapedNodeData alMasihi = Scraper.scrapeNode(310782); //Fails because there is unknown year of completion!
        assertEquals("Abu Sahl 'Isa ibn Yahya al-Masihi", alMasihi.name());
        assertEquals(1, alMasihi.dissertations().size());
        assertNull(alMasihi.dissertations().get(0).dissertationtitle());
        assertNull(alMasihi.dissertations().get(0).phdprefix());
        assertNull(alMasihi.dissertations().get(0).yearofcompletion());
        assertNull(alMasihi.dissertations().get(0).advisor1id());
        assertNull(alMasihi.dissertations().get(0).advisor1name());
        assertNull(alMasihi.dissertations().get(0).advisor2id());
        assertNull(alMasihi.dissertations().get(0).advisor2name());
        assertEquals(1, alMasihi.students().size());
        assertEquals("Abu ʿAli al-Husayn ibn Sina", alMasihi.students().get(0).student().name());
        assertTrue(alMasihi.numberofdescendents() >= 226493);
    }

    @Test
    void scrapeDonaldson_Scraping_should_have_no_MSC_number() throws IOException , NodeDoesNotExistException {
        ScrapedNodeData donaldson = Scraper.scrapeNode(36909);
        assertNull(donaldson.dissertations().get(0).mscnumber());
    }

    @Test
    void scrapeAtiyah_Should_have_exactly_26_students_and_at_least_1061_descendents() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData atiyah = Scraper.scrapeNode(30949);
        assertEquals(26, atiyah.students().size());
        assertTrue(atiyah.numberofdescendents() >= 1061);
    }

    @Test
    void scrapeLeibniz_Should_have_3_dissertations_and_second_dissertation_should_have_one_advisor_and_third_dissertation_should_have_no_title() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData leibniz = Scraper.scrapeNode(60985);

        assertEquals(3, leibniz.dissertations().size());

        assertNotNull(leibniz.dissertations().get(0).advisor1name());
        assertEquals("Disputatio arithmetica de complexionibus", leibniz.dissertations().get(0).dissertationtitle());

        assertNull(leibniz.dissertations().get(1).advisor2id());
        assertNull(leibniz.dissertations().get(1).advisor2name());
        assertEquals("Dr. jur.", leibniz.dissertations().get(1).phdprefix());

        assertNull(leibniz.dissertations().get(2).dissertationtitle());
        assertNull(leibniz.dissertations().get(2).phdprefix());
        assertEquals("Académie royale des sciences de Paris", leibniz.dissertations().get(2).university());
        assertEquals(1676, leibniz.dissertations().get(2).yearofcompletion());
        assertEquals("Christiaan Huygens", leibniz.dissertations().get(2).advisor1name());
        assertNull(leibniz.dissertations().get(2).advisor2id());
    }

    @Test
    void scrapeAlHusayn_Should_succeed_scraping_two_advisors_even_though_three_advisors_are_listed() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData alHusayn = Scraper.scrapeNode(298616);

        assertNull(alHusayn.dissertations().get(0).dissertationtitle());
        assertEquals("Abu Abdallah Al-Husayn ibn Ibrahim al-Natili", alHusayn.dissertations().get(0).advisor1name());
        assertEquals("Abu Sahl 'Isa ibn Yahya al-Masihi", alHusayn.dissertations().get(0).advisor2name());
    }
}