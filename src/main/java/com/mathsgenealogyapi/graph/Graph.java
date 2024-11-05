package com.mathsgenealogyapi.graph;

import com.mathsgenealogyapi.edge.Edge;
import com.mathsgenealogyapi.node.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/*
    Holds the structure of a graph including the Node and Edge and Dissertation entities. Will be converted to a GraphDto to be outputted by the API in JSON form.
 */
@Getter
@Setter
@AllArgsConstructor
public class Graph {
    private Integer base;
    private List<Node> nodes;
    private List<Edge> edges;

    private Integer generationsUp;

    private Integer generationsDown;

    public Integer getNumberOfNodes() { return nodes.size(); }

    public Integer getNumberOfEdges() { return edges.size(); }
}
