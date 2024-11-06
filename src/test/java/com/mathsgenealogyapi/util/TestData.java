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
    List<TestDataEdge> testEdges
) {

    public static TestData fromArray(int[][][] array) {
        List<TestDataNode> testNodes = new ArrayList<>();
        List<TestDataEdge> testEdges = new ArrayList<>();

        for (int[] node : array[0]) {
            testNodes.add(new TestDataNode(node[0], node[1] != 0, node[2] != 0));
        }
        for (int[] edge : array[1]) {
            testEdges.add(new TestDataEdge(edge[0], edge[1]));
        }
        return new TestData(testNodes, testEdges);
    }

    private List<TestDataEdge> edgesWithFromId(int id) {
        return testEdges.stream().filter(it -> it.fromId() == id).toList();
    }

    private List<TestDataEdge> edgesWithToId(int id) {
        return testEdges.stream().filter(it -> it.toId() == id).toList();
    }

    public List<Node> generateEntities() { // Creates data to be persisted to the database from simple test data
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
    }

}
