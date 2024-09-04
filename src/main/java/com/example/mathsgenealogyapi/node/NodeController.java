package com.example.mathsgenealogyapi.node;

import com.example.mathsgenealogyapi.Constants;
import com.example.mathsgenealogyapi.dissertation.Dissertation;
import com.example.mathsgenealogyapi.dissertation.DissertationRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class NodeDoesNotExistException extends Exception { }

@RestController
@RequestMapping("/api/nodes")
public class NodeController {

    @Autowired
    private NodeRepository nodeRepository;

    @GetMapping("/{id}")
    public Node getEntryById(@RequestParam Long id) {
        Node requested = nodeRepository.findById(id).get();
        if (requested.getLastupdated().isBefore(LocalDateTime.now().minusDays(Constants.daysToInvalidateCache))) {
            return forceUpdate(id);
        }
        else {
            return requested;
        }
    }

    private Node forceUpdate(Long id) {
        Node existingNode = nodeRepository.findById(id).get();
        try {
            existingNode = scrapeEntry(id);
        }
        catch (IOException | NodeDoesNotExistException e) {
            // scraping failed
        }

        return existingNode;

    }

    public Node test(Long id) throws IOException, NodeDoesNotExistException {
        return scrapeEntry(id);
    }

    private Node scrapeEntry(Long id) throws IOException, NodeDoesNotExistException {
        Node constructedNode = new Node();
        List<Dissertation> dissertations = new ArrayList();
        Dissertation newDissertation = new Dissertation();
        dissertations.add(newDissertation);
        constructedNode.setGenealogyId(id);
        constructedNode.setDissertations(dissertations);
        newDissertation.setNode(constructedNode);


        Document webpage = Jsoup.connect("https://www.mathgenealogy.org/id.php?id=" + id).get();
        if (Objects.equals(webpage.html(), "<p>You have specified an ID that does not exist in the database. Please back up and try again.</p>")) {
            throw new NodeDoesNotExistException();
        }

        System.out.println("Downloaded webpage for id " + id);

        Element content = webpage.getElementById("mainContent").child(0);

        String name = content.getElementsByTag("h2").first().ownText().trim();
        String phdprefix = content.getElementsByTag("div").get(2).child(0).textNodes().get(0).text().trim();
        String university = content.getElementsByTag("div").get(2).child(0).child(0).text().trim();
        Integer yearofcompletion = Integer.valueOf(content.getElementsByTag("div").get(2).child(0).textNodes().get(1).text().trim());
        String dissertationtitle = content.getElementById("thesisTitle").text().trim();

        String mscnumber = content.getElementsByTag("div").get(4).text().split(" ")[3].split("—")[0];

        Element advisors = content.getElementsByTag("p").get(1);
        String advisor1name = "";
        Long advisor1id = 0L;
        String advisor2name = "";
        Long advisor2id = 0L;
        if (advisors.childrenSize() > 0) {
            Element advisor1 = advisors.child(0);
            advisor1name = advisor1.text().trim();
            advisor1id = Long.valueOf(advisor1.attribute("href").getValue().split("=")[1]);
            if (advisors.childrenSize() > 2) {
                Element advisor2 = advisors.child(2);
                advisor2name = advisor2.text().trim();
                advisor2id = Long.valueOf(advisor2.attribute("href").getValue().split("=")[1]);
            }
        }



        List<Integer> students = new ArrayList<Integer>();
        if (!content.getElementsByTag("table").isEmpty()) {
            Elements studentList = content.getElementsByTag("table").get(0).getElementsByTag("tr");
            studentList.remove(0);
            for (Element element : studentList) {
                students.add(Integer.valueOf(element.getElementsByTag("a").get(0).attribute("href").getValue().split("=")[1]));
            }
        }


        String descendentstext;
        if (students.isEmpty()) {
            descendentstext = content.getElementsByTag("p").get(2).textNodes().get(0).text();
        }
        else {
            descendentstext = content.getElementsByTag("p").get(3).textNodes().get(0).text();
        }

        Integer numberofdescendents;
        if (Objects.equals(descendentstext, "No students known.")) {
            numberofdescendents = 0;
        }
        else {
            System.out.println(descendentstext);
            String[] split = descendentstext.split(" ");
            numberofdescendents = Integer.valueOf(split[split.length-2]);
        }




        constructedNode.setName(name);
        newDissertation.setPhdprefix(phdprefix);
        newDissertation.setUniversity(university);
        newDissertation.setYearofcompletion(yearofcompletion);
        newDissertation.setDissertationtitle(dissertationtitle);
        newDissertation.setAdvisor1name(advisor1name);
        newDissertation.setAdvisor1id(advisor1id);
        newDissertation.setAdvisor2name(advisor2name);
        newDissertation.setAdvisor2id(advisor2id);
        constructedNode.setNumberofdescendents(numberofdescendents);
        newDissertation.setMscnumber(mscnumber);
        constructedNode.setStudents(students);

        return constructedNode;
    }
}
