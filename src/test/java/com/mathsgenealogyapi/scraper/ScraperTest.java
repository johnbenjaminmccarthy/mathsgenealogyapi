package com.mathsgenealogyapi.scraper;

import com.mathsgenealogyapi.NodeDoesNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScraperTest {

    private static final Logger logger = LogManager.getLogger(ScraperTest.class);

    Scraper scraper = Mockito.spy(Scraper.class);
    @BeforeAll
    public void init() throws IOException {
        doAnswer(invocation -> testDocument((Integer)invocation.getArguments()[0])).when(scraper).getDocument(anyInt());
    }

    Document testDocument(Integer id) throws IOException {
        logger.info("Intercepted scrape connection and returned test file " + id + ".html");
        File testFile = new File("src/test/resources/scrapeTests/" + id + ".html");
        return Jsoup.parse(testFile, "UTF-8", "http://mathsgenealogy.com/");
    }

    @Test
    void scrapeJohn_Scraping_should_return_name_and_advisors_and_one_dissertation_and_no_students() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData john = scraper.scrapeNode(293462);

        assertEquals("John Benjamin McCarthy",john.name());
        assertEquals(293462,john.id());
        assertEquals(1, john.dissertations().size());
        assertEquals(john.id(), john.dissertations().get(0).studentId());
        assertEquals(john.name(), john.dissertations().get(0).studentName());
        assertEquals("Stability conditions and canonical metrics", john.dissertations().get(0).dissertationtitle());
        assertEquals("Ph.D.",john.dissertations().get(0).phdprefix());
        assertEquals("Imperial College London",john.dissertations().get(0).university());
        assertEquals("2023",john.dissertations().get(0).yearofcompletion());
        assertEquals("53",john.dissertations().get(0).mscnumber());

        assertEquals(2, john.dissertations().get(0).advisors().size());

        Optional<ScrapedAdvisorData> advisor1 = john.dissertations().get(0).getAdvisorByNumber(1);
        assertTrue(advisor1.isPresent());
        assertEquals(36909, advisor1.get().advisorId());
        assertEquals("Simon Kirwan Donaldson", advisor1.get().name());

        Optional<ScrapedAdvisorData> advisor2 = john.dissertations().get(0).getAdvisorByNumber(2);
        assertTrue(advisor2.isPresent());
        assertEquals(217413, advisor2.get().advisorId());
        assertEquals("Ruadhaí Dervan", advisor2.get().name());

        Optional<ScrapedAdvisorData> advisor3 = john.dissertations().get(0).getAdvisorByNumber(3);
        assertTrue(advisor3.isEmpty());

        assertEquals(0,john.numberofdescendents());
        assertTrue(john.students().isEmpty());
    }

    @Test
    void scrapeRuadhai_Scraping_should_return_name_with_special_character_and_at_least_3_descendents_and_have_null_second_advisor_name_and_id() throws IOException , NodeDoesNotExistException {
        ScrapedNodeData ruadhai = scraper.scrapeNode(217413);
        assertEquals("Ruadhaí Dervan", ruadhai.name());
        assertTrue(ruadhai.numberofdescendents() >= 3);
        assertTrue(ruadhai.dissertations().get(0).getAdvisorByNumber(1).isPresent());
        assertTrue(ruadhai.dissertations().get(0).getAdvisorByNumber(2).isEmpty());
    }

    @Test
    void scrapeAlMasihi_Scraping_should_have_dissertation_with_no_advisors_or_title() throws NodeDoesNotExistException, IOException {
        ScrapedNodeData alMasihi = scraper.scrapeNode(310782); //Fails because there is unknown year of completion!
        assertEquals("Abu Sahl 'Isa ibn Yahya al-Masihi", alMasihi.name());
        assertEquals(1, alMasihi.dissertations().size());
        assertNull(alMasihi.dissertations().get(0).dissertationtitle());
        assertNull(alMasihi.dissertations().get(0).phdprefix());
        assertNull(alMasihi.dissertations().get(0).yearofcompletion());
        assertTrue(alMasihi.dissertations().get(0).advisors().isEmpty());
        assertEquals(1, alMasihi.students().size());
        assertEquals("Abu ʿAli al-Husayn ibn Sina", alMasihi.students().get(0).student().name());
        assertTrue(alMasihi.numberofdescendents() >= 226493);
    }

    @Test
    void scrapeDonaldson_Scraping_should_have_no_MSC_number() throws IOException , NodeDoesNotExistException {
        ScrapedNodeData donaldson = scraper.scrapeNode(36909);
        assertNull(donaldson.dissertations().get(0).mscnumber());
    }

    @Test
    void scrapeAtiyah_Should_have_exactly_26_students_and_at_least_1061_descendents() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData atiyah = scraper.scrapeNode(30949);
        assertEquals(26, atiyah.students().size());
        assertTrue(atiyah.numberofdescendents() >= 1061);
    }

    @Test
    void scrapeLeibniz_Should_have_3_dissertations_and_second_dissertation_should_have_one_advisor_and_third_dissertation_should_have_no_title() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData leibniz = scraper.scrapeNode(60985);

        assertEquals(3, leibniz.dissertations().size());

        Optional<ScrapedAdvisorData> dissertation1advisor1 = leibniz.dissertations().get(0).getAdvisorByNumber(1);
        assertTrue(dissertation1advisor1.isPresent());
        assertNotNull(dissertation1advisor1.get().name());
        assertEquals("Disputatio arithmetica de complexionibus", leibniz.dissertations().get(0).dissertationtitle());

        Optional<ScrapedAdvisorData> dissertation2advisor2 = leibniz.dissertations().get(1).getAdvisorByNumber(2);
        assertTrue(dissertation2advisor2.isEmpty());
        assertEquals("Dr. jur.", leibniz.dissertations().get(1).phdprefix());

        assertNull(leibniz.dissertations().get(2).dissertationtitle());
        assertNull(leibniz.dissertations().get(2).phdprefix());
        assertEquals("Académie royale des sciences de Paris", leibniz.dissertations().get(2).university());
        assertEquals("1676", leibniz.dissertations().get(2).yearofcompletion());
        Optional<ScrapedAdvisorData> dissertation3advisor1 = leibniz.dissertations().get(2).getAdvisorByNumber(1);
        assertTrue(dissertation3advisor1.isPresent());
        assertEquals("Christiaan Huygens", dissertation3advisor1.get().name());
    }

    @Test
    void scrapeAlHusayn_Should_succeed_scraping_three_advisors() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData alHusayn = scraper.scrapeNode(298616);

        assertNull(alHusayn.dissertations().get(0).dissertationtitle());
        assertTrue(alHusayn.dissertations().get(0).getAdvisorByNumber(1).isPresent());
        assertTrue(alHusayn.dissertations().get(0).getAdvisorByNumber(2).isPresent());
        assertTrue(alHusayn.dissertations().get(0).getAdvisorByNumber(3).isPresent());
    }

    @Test
    void scrapeCayley_shouldHaveDissertationYearWithSlashes() throws IOException, NodeDoesNotExistException {
        ScrapedNodeData Cayley = scraper.scrapeNode(7824);

        assertEquals("1864/1865/1875", Cayley.dissertations().get(0).yearofcompletion());
    }
}