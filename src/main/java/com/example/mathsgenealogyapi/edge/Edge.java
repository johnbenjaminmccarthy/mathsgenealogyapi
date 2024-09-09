package com.example.mathsgenealogyapi.edge;

import com.example.mathsgenealogyapi.node.Node;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(EdgeId.class)
@Table(name = "edges")
public class Edge {

    @Id
    @Column(name="fromid")
    private Long fromid;

    @Id
    @Column(name="toid")
    private Long toid;
}
