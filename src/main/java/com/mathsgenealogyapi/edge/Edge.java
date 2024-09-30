package com.mathsgenealogyapi.edge;

import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.node.Node;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(EdgeId.class)
@Table(name = "edges")
public class Edge {

    @Id
    @ManyToOne
    @JoinColumn(name = "fromNode_id", nullable = false)
    private Node fromNode;

    @Id
    @ManyToOne
    @JoinColumn(name = "toNode_id", nullable = false)
    private Node toNode;

    public Edge(Node fromNode, Node toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }
}
