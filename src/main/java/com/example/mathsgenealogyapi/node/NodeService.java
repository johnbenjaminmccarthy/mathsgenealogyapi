package com.example.mathsgenealogyapi.node;

import com.example.mathsgenealogyapi.Constants;
import com.example.mathsgenealogyapi.dissertation.Dissertation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


class NodeDoesNotExistException extends Exception { }


@Service
@Transactional
public class NodeService {
    @Autowired
    NodeRepository repository;
    @Autowired
    ConversionService conversionService;

    private Node forceUpdate(Integer id) {
        Node existingNode = repository.findById(id).get();
        try {
            existingNode = scrapeEntry(id).get();
        }
        catch (IOException e) {
            // scraping failed
        }

        return existingNode;

    }

    public Node test(Integer id) throws IOException, NodeDoesNotExistException {
        return scrapeEntry(id).get();
    }

    private Optional<Node> scrapeEntry(Integer id) throws IOException {
        Node constructedNode = new Node();
        List<Dissertation> dissertations = new ArrayList();
        Dissertation newDissertation = new Dissertation();
        dissertations.add(newDissertation);
        constructedNode.setGenealogyId(id);
        constructedNode.setDissertations(dissertations);
        newDissertation.setNode(constructedNode);

        Document webpage = Jsoup.connect("https://www.mathgenealogy.org/id.php?id=" + id).get();
        if (Objects.equals(webpage.html(), "<p>You have specified an ID that does not exist in the database. Please back up and try again.</p>")) {
            return Optional.empty();
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
        Integer advisor1id = 0;
        String advisor2name = "";
        Integer advisor2id = 0;
        if (advisors.childrenSize() > 0) {
            Element advisor1 = advisors.child(0);
            advisor1name = advisor1.text().trim();
            advisor1id = Integer.valueOf(advisor1.attribute("href").getValue().split("=")[1]);
            if (advisors.childrenSize() > 2) {
                Element advisor2 = advisors.child(2);
                advisor2name = advisor2.text().trim();
                advisor2id = Integer.valueOf(advisor2.attribute("href").getValue().split("=")[1]);
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

        return Optional.of(constructedNode);
    }

    private Node addOrUpdateNode(Node newNode) {
        return repository.save(newNode);
    }

    public NodeDto getSingleNode(Integer id) throws NodeDoesNotExistException, IOException {
        Optional<Node> requested = repository.findById(id);
        if (requested.isPresent() && requested.get().getLastupdated().isAfter(LocalDateTime.now().minusDays(Constants.daysToInvalidateCache))) {
            return conversionService.convert(requested.get(), NodeDto.class);
        }
        else {
            Optional<Node> scrapedNode = scrapeEntry(id); //TODO: Update entry instead of creating new entry. scrapeEntry returns a *new* Node object instead of modifying the properties of the existing Node object from requested, if it exists.
            if (scrapedNode.isEmpty()) {
                if (requested.isEmpty()) {
                    throw new NodeDoesNotExistException();
                }
                else {
                    return conversionService.convert(requested.get(), NodeDto.class); //If scraping failed for some reason serve the existing node data
                }
            }
            else {
                return conversionService.convert(addOrUpdateNode(scrapedNode.get()), NodeDto.class);
            }
        }

    }
}
