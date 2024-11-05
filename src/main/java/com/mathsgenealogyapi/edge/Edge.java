package com.mathsgenealogyapi.edge;

import com.mathsgenealogyapi.advisor.Advisor;
import com.mathsgenealogyapi.node.Node;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(EdgeId.class)
@Table(name = "edges")
public class Edge {

    @Id
    @ManyToOne
    @JoinColumn(name = "from_node_id", nullable = false)
    private Node fromNode;

    @Id
    @ManyToOne
    @JoinColumn(name = "to_node_id", nullable = false)
    private Node toNode;

    @OneToMany(mappedBy = "advisorEdge", fetch = FetchType.LAZY)
    private List<Advisor> advisors;

    public Edge(Node fromNode, Node toNode) {
        this.fromNode = fromNode;
        this.toNode = toNode;
    }
}
