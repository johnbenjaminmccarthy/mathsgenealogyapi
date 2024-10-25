package com.mathsgenealogyapi.node;

import com.mathsgenealogyapi.graph.Graph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {

    @Transactional
    default Node getOrInsert(Node node) {
        return this.findById(node.getId())
                .orElseGet(() -> this.save(node));
    }

    @Query(value =
            "WITH RECURSIVE\n" +
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
            "SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, -generationsDown AS generationsCount FROM nodes JOIN descendents ON nodes.genealogy_id = descendents.to_node_id\n" +
            "UNION SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, generationsUp AS generationsCount FROM nodes JOIN ancestors ON nodes.genealogy_id = ancestors.from_node_id\n" +
            "UNION SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, 0 AS generationsCount FROM nodes WHERE nodes.genealogy_id = :baseId"
            , nativeQuery = true)
    List<Node> getNodes(@Param("baseId") Integer base, @Param("maxGenerationsDown") Integer maxGenerationsDown, @Param("maxGenerationsUp") Integer maxGenerationsUp);

    default List<Node> getNodes(Integer base) {
        return getNodes(base, 5, 5);
    }

    Graph getGraph(Integer id, Integer maxGenerationsUp, Integer maxGenerationsDown);
}
