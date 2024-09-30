package com.mathsgenealogyapi.graph;


import com.mathsgenealogyapi.edge.EdgeDto;
import com.mathsgenealogyapi.node.NodeDto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Graph}
 */
public record GraphDto(
        Integer base, //The base of the graph i.e. the focus of the tree.
        Integer generationsUp, //The largest depth up the tree in generations from the base
        Integer generationsDown, //The largest depth down the tree in generations from the base
        List<NodeDto> nodes,
        List<EdgeDto> edges

) implements Serializable {
}
