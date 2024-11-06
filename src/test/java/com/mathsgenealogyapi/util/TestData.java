package com.mathsgenealogyapi.util;



import com.mathsgenealogyapi.Constants;
import com.mathsgenealogyapi.advisor.Advisor;
import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.edge.Edge;
import com.mathsgenealogyapi.node.Node;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

public record TestData (
    List<TestDataNode> testNodes,
    List<TestDataEdge> testEdges,

    List<TestDataDissertation> testDissertations,

    List<TestDataAdvisor> testAdvisors
) {

    /*
        Takes in an array of data in the form
        data = {nodes, edges}
        where
            nodes = array of node and node={a,b,c} for a=id, b=upToDate boolean (1 or 0), c=scraped boolean (1 or 0).
            edges = array of edge, edge = {a,b} where a = fromid and b = toid

        Generates random data associated with this data
     */
    public static TestData fromArray(int[][][] array) {
        List<TestDataNode> testNodes = new ArrayList<>();
        List<TestDataEdge> testEdges = new ArrayList<>();
        List<TestDataDissertation> testDissertations = new ArrayList<>();
        List<TestDataAdvisor> testAdvisors = new ArrayList<>();


        for (int[] node : array[0]) {
            testNodes.add(new TestDataNode(
                    node[0],
                    node[1] != 0 ? LocalDateTime.now() : LocalDateTime.now().minusDays(Constants.daysToInvalidateCache + 1),
                    RandomStringUtils.randomAlphabetic(10),
                    0,
                    node[2] != 0));
            testDissertations.add(new TestDataDissertation(
                    node[0],
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    RandomStringUtils.randomAlphabetic(10),
                    node[0]
            ));
        }
        int i = 1;
        for (int[] edge : array[1]) {
            testEdges.add(new TestDataEdge(edge[0], edge[1]));
            testAdvisors.add(new TestDataAdvisor(
                    i,
                    RandomStringUtils.randomAlphabetic(10),
                    edge[1],
                    edge[0],
                    edge[1]
            ));
            i++;
        }

        return new TestData(testNodes, testEdges, testDissertations, testAdvisors);
    }

    public String bulkInsertSQL() {
        StringBuilder SQL = new StringBuilder();

        SQL.append("INSERT INTO nodes (genealogy_id, lastupdated, name, numberofdescendents, scraped) VALUES\n");
        int i = 0;
        for (TestDataNode node: this.testNodes) {
            SQL.append(String.format("(%d,'%s','%s',%d,%B)",
                    node.genealogy_id(),
                    node.lastupdated().toString(),
                    node.name(),
                    node.numberofdescendents(),
                    node.scraped()));
            if (i < testNodes.size()-1) {
                SQL.append(",\n");
            }
            else {
                SQL.append(";\n");
            }
            i++;
        }

        SQL.append("INSERT INTO edges (from_node_id, to_node_id) VALUES\n");
        i = 0;
        for (TestDataEdge edge: this.testEdges) {
            SQL.append(String.format("(%d,%d)",
                    edge.from_node_id(),
                    edge.to_node_id()));
            if (i < this.testEdges.size()-1) {
                SQL.append(",\n");
            }
            else {
                SQL.append(";\n");
            }
            i++;
        }

        SQL.append("INSERT INTO dissertations (id, dissertationtitle, mscnumber, phdprefix, university, yearofcompletion, node_id) VALUES\n");
        i = 0;
        for (TestDataDissertation dissertation: this.testDissertations) {
            SQL.append(String.format("(%d,'%s','%s','%s','%s','%s',%d)",
                    dissertation.id(),
                    dissertation.dissertationtitle(),
                    dissertation.mscnumber(),
                    dissertation.phdprefix(),
                    dissertation.university(),
                    dissertation.yearofcompletion(),
                    dissertation.node_id()));
            if (i < this.testDissertations.size()-1) {
                SQL.append(",\n");
            }
            else {
                SQL.append(";\n");
            }
            i++;
        }

        SQL.append("INSERT INTO advisors (dissertation_id, advisor_edge_from_node_id, advisor_edge_to_node_id, name, advisor_number) VALUES\n");
        i = 0;
        for (TestDataAdvisor advisor: this.testAdvisors) {
            SQL.append(String.format("(%d,%d,%d,'%s',%d)",
                    advisor.dissertation_id(),
                    advisor.advisor_edge_from_node_id(),
                    advisor.advisor_edge_to_node_id(),
                    advisor.name(),
                    advisor.advisor_number()));
            if (i < this.testAdvisors.size()-1) {
                SQL.append(",\n");
            }
            else {
                SQL.append(";\n");
            }
            i++;
        }
        return SQL.toString();
    }

    private List<TestDataEdge> edgesWithFromId(int id) {
        return testEdges.stream().filter(it -> it.from_node_id() == id).toList();
    }

    private List<TestDataEdge> edgesWithToId(int id) {
        return testEdges.stream().filter(it -> it.to_node_id() == id).toList();
    }

    /*public List<Node> generateEntities() { // Creates data to be persisted to the database from simple test data
        Map<Integer, Node> nodes = new HashMap<>();
        Map<Integer, List<Edge>> advisorEdges = new HashMap<>(); //Key: childId, Value: Edges corresponding to parents of child
        Map<Integer, List<Edge>> studentEdges = new HashMap<>(); //Key: parentId, Value: Edges corresponding to children of parent

        for(TestDataNode testNode : this.testNodes) {
            Node node = new Node(testNode.id());
            node.setLastupdated(testNode.upToDate() ? LocalDateTime.now() : LocalDateTime.now().minusDays(Constants.daysToInvalidateCache + 1));
            node.setScraped(testNode.scraped());
            node.setName(RandomStringUtils.randomAlphabetic(10));
            node.setNumberofdescendents(0);

            nodes.put(testNode.id(), node);
        }
        for(TestDataEdge testEdge : this.testEdges) {
            Edge edge = new Edge(nodes.get(testEdge.fromId()), nodes.get(testEdge.toId()));

            if (advisorEdges.containsKey(testEdge.toId())) {
                advisorEdges.get(testEdge.toId()).add(edge);
            }
            else {
                advisorEdges.put(testEdge.toId(), new ArrayList<>(Collections.singletonList(edge)));
            }

            if (studentEdges.containsKey(testEdge.fromId())) {
                studentEdges.get(testEdge.fromId()).add(edge);
            }
            else {
                studentEdges.put(testEdge.fromId(), new ArrayList<>(Collections.singletonList(edge)));
            }
        }

        for(Node node : nodes.values()) {

            Dissertation dissertation = randomDissertation(node);

            List<Edge> nodeAdvisorEdges = advisorEdges.getOrDefault(node.getId(), new ArrayList<>());
            List<Advisor> advisors = new ArrayList<>();
            int i = 1;
            for (Edge edge : nodeAdvisorEdges) {
                Advisor advisor = randomAdvisor(dissertation, edge, i);
                advisors.add(advisor);
                //edge.setAdvisors(Collections.singletonList(advisor));
                i++;
            }

            dissertation.setAdvisors(advisors);
            node.setAdvisorEdges(nodeAdvisorEdges);

            List<Edge> nodeStudentEdges = studentEdges.getOrDefault(node.getId(), new ArrayList<>());
            node.setStudentEdges(nodeStudentEdges);

            node.setDissertations(new ArrayList<>(Collections.singletonList(dissertation)));
        }


        return nodes.values().stream().toList();
    }

    private Advisor randomAdvisor(Dissertation dissertation, Edge edge, Integer advisorNumber) {
        Advisor advisor = new Advisor();
        advisor.setAdvisorEdge(edge);
        advisor.setName(RandomStringUtils.randomAlphabetic(10));
        advisor.setDissertation(dissertation);
        advisor.setAdvisorNumber(advisorNumber);
        return advisor;
    }

    private Dissertation randomDissertation(Node node) {
        Dissertation dissertation = new Dissertation();
        dissertation.setNode(node);
        dissertation.setDissertationtitle(RandomStringUtils.randomAlphabetic(10));
        dissertation.setUniversity(RandomStringUtils.randomAlphabetic(10));
        dissertation.setMscnumber(RandomStringUtils.randomAlphabetic(10));
        dissertation.setPhdprefix(RandomStringUtils.randomAlphabetic(10));
        dissertation.setYearofcompletion(RandomStringUtils.randomAlphabetic(10));
        return dissertation;
    }*/

}
