package com.mathsgenealogyapi.scraper;

import com.mathsgenealogyapi.Student;
import com.mathsgenealogyapi.NodeDoesNotExistException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class Scraper {

    private static final Logger logger = LogManager.getLogger(Scraper.class);



    private Scraper() {

    }

    static public ScrapedNodeData scrapeNode(Integer id) throws NodeDoesNotExistException, IOException {


        Document webpage = Jsoup.connect("https://www.mathgenealogy.org/id.php?id=" + id).get();
        logger.info("Downloaded webpage for id " + id);

        if (Objects.equals(webpage.html(), "<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>You have specified an ID that does not exist in the database. Please back up and try again.</p>\n" +
                " </body>\n" +
                "</html>")) {
            throw new NodeDoesNotExistException();
        }

        Element content = webpage.getElementById("mainContent").child(0);

        /*
            Get name
         */
        String name = content.getElementsByTag("h2").first().ownText().trim();


        /*
            Begin scraping advisor data
         */
        List<ScrapedDissertationData> dissertations = new ArrayList<ScrapedDissertationData>();

        Integer i = 5;

        while (!content.child(i).text().startsWith("Student") && !content.child(i).text().startsWith("No students known.")) {
            String phdprefix = content.child(i).child(0).textNodes().get(0).text().trim();
            if (phdprefix.isEmpty()) {
                phdprefix = null;
            }

            String university = content.child(i).child(0).child(0).text().trim();
            if (university.isEmpty()) {
                university = null;
            }

            String yearofcompletionStr = content.child(i).child(0).textNodes().get(1).text().trim();
            String yearofcompletion;
            if (yearofcompletionStr.isEmpty()) {
                yearofcompletion = null;
            }
            else {
                yearofcompletion = yearofcompletionStr;
            }

            i++;

            String dissertationtitle = content.child(i).getElementById("thesisTitle").text().trim();
            if (dissertationtitle.isEmpty()) {
                dissertationtitle = null;
            }

            i++;
            String mscnumber = null;
            if (content.child(i).text().startsWith("Mathematics Subject Classification")) {
                mscnumber = content.child(i).text().split(" ")[3].split("—")[0];
                i++;
            }
            String advisor1name = null;
            Integer advisor1id = null;
            String advisor2name = null;
            Integer advisor2id = null;
            if (content.child(i).childrenSize() >= 1) {
                Element advisor1 = content.child(i).child(0);
                advisor1name = advisor1.text().trim();
                advisor1id = Integer.valueOf(advisor1.attribute("href").getValue().split("=")[1]);
            }
            if (content.child(i).childrenSize() >= 3) {
                Element advisor2 = content.child(i).child(2);
                advisor2name = advisor2.text().trim();
                advisor2id = Integer.valueOf(advisor2.attribute("href").getValue().split("=")[1]);
            }


            ScrapedDissertationData dissertation = new ScrapedDissertationData(
                    id,
                    name,
                    phdprefix,
                    university,
                    yearofcompletion,
                    dissertationtitle,
                    mscnumber,
                    advisor1name,
                    advisor1id,
                    advisor2name,
                    advisor2id

            );

            dissertations.add(dissertation);

            i++;
        }

        /*
            Begin scraping student data
         */
        List<ScrapedStudentData> students = new ArrayList<ScrapedStudentData>();
        if (!content.getElementsByTag("table").isEmpty()) {
            Elements studentList = content.getElementsByTag("table").get(0).getElementsByTag("tr");
            studentList.remove(0);
            for (Element element : studentList) {
                Integer studentId = Integer.valueOf(element.getElementsByTag("a").get(0).attribute("href").getValue().split("=")[1]);
                String[] studentNames = element.getElementsByTag("a").get(0).text().split(","); //["LastName", " FirstName"]
                String studentName = "";
                for (String namePart : studentNames) {
                    studentName = namePart.trim() + " " + studentName;
                }
                studentName = studentName.trim();

                String studentUniversity = element.getElementsByTag("td").get(1).text();

                String studentYearofcompletion = element.getElementsByTag("td").get(2).text();

                Integer studentNumberofdescendents = null;
                String studentNumberofdescendentsStr = element.getElementsByTag("td").get(3).text();
                if (!Objects.equals(studentNumberofdescendentsStr, "")) {
                    studentNumberofdescendents = Integer.valueOf(studentNumberofdescendentsStr);
                }

                students.add(new ScrapedStudentData(
                        new Student(studentId, studentName),
                        studentUniversity,
                        studentYearofcompletion,
                        studentNumberofdescendents
                ));
            }
        }


        /*
            Get number of descendents
         */
        String descendentstext;
        Integer numberofdescendents = 0;
        if (!students.isEmpty()) {
            String[] split = content.getElementsByTag("table").get(0).nextElementSibling().textNodes().get(0).text().split(" ");
            numberofdescendents = Integer.valueOf(split[split.length-2]);
        }


        return new ScrapedNodeData(id, name, numberofdescendents, dissertations, students);

    }
}
