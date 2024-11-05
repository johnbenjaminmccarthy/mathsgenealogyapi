package com.mathsgenealogyapi.util;



import com.mathsgenealogyapi.node.Node;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public record TestData (
    List<TestDataNode> testNodes,
    List<TestDataEdge> testEdges
) {

    public static TestData fromArray(int[][][] array) throws IOException {


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

    public List<Node> generateEntities() { // Creates data to be persisted to the database from simple test data
        List<Node> nodes = new ArrayList<>();



        //RandomStringUtils.randomAlphabetic(10);
        return nodes;
    }

}
