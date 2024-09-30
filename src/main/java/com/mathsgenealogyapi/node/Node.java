package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.dissertation.Dissertation;
import com.mathsgenealogyapi.edge.Edge;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "nodes")
public class Node {
    @Id
    @Column(name = "genealogyId", unique = true)
    private Integer id; //The genealogy id on https://genealogy.math.ndsu.nodak.edu/

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "node")
    private List<Dissertation> dissertations;

    @OneToMany(mappedBy = "fromNode", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Edge> studentEdges;

    @OneToMany(mappedBy = "toNode", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Edge> advisorEdges;

    @Column(name = "numberofdescendents")
    private Integer numberofdescendents;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastupdated")
    private LocalDateTime lastupdated;

    @Column(name = "scraped", nullable = false)
    private Boolean scraped;

    public Node(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.scraped = false;
    }

    public Node(Integer id) {
        this.id = id;
        this.scraped = false;
    }
}
