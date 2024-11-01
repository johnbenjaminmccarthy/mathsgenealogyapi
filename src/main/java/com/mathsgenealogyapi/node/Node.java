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




@NamedNativeQuery(
        name="Node.getNodes",
        resultSetMapping = "NodesMapping",
        query="WITH RECURSIVE\n" +
                "    descendents AS (\n" +
                "        SELECT edges.to_node_id, 1 AS generationsDown FROM edges WHERE edges.from_node_id = :baseId\n" +
                "        UNION\n" +
                "        SELECT edges.to_node_id, generationsDown + 1 AS generationsDown FROM edges\n" +
                "        JOIN descendents ON edges.from_node_id = descendents.to_node_id\n" +
                "WHERE descendents.generationsDown < :maxGenerationsDown\n" +
                "    ),\n" +
                "    ancestors AS (\n" +
                "        SELECT edges.from_node_id, 1 AS generationsUp FROM edges WHERE edges.to_node_id = :baseId\n" +
                "        UNION\n" +
                "        SELECT edges.from_node_id, generationsUp + 1 AS generationsUp FROM edges\n" +
                "                                           JOIN ancestors ON edges.to_node_id = ancestors.from_node_id\n" +
                "WHERE ancestors.generationsUp < :maxGenerationsUp\n" +
                "    )\n" +
                "SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, -generationsDown AS generationscount FROM nodes JOIN descendents ON nodes.genealogy_id = descendents.to_node_id\n" +
                "UNION SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, generationsUp AS generationscount FROM nodes JOIN ancestors ON nodes.genealogy_id = ancestors.from_node_id\n" +
                "UNION SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, 0 AS generationscount FROM nodes WHERE nodes.genealogy_id = :baseId"
)
@SqlResultSetMapping(
        name = "NodesMapping",
        entities={
                @EntityResult(
                        entityClass = Node.class,
                        fields={
                                @FieldResult(name="id", column="genealogy_id"),
                                @FieldResult(name="lastupdated", column="lastupdated"),
                                @FieldResult(name="name", column="name"),
                                @FieldResult(name="numberofdescendents", column="numberofdescendents"),
                                @FieldResult(name="scraped", column="scraped")
                        }
                )
        },
        columns={
                @ColumnResult(name="generationscount")
        }
)
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

    @Transient
    private Integer generationscount;

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
