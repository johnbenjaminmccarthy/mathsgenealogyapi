package com.mathsgenealogyapi.node;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {

    Logger logger = LogManager.getLogger(NodeRepository.class);

    @Transactional
    default Node getOrInsert(Node node) {
        return this.findById(node.getId())
                .orElseGet(() -> this.saveAndLog(node, "getOrInsert"));
    }

    default Node saveAndLog(Node node, String extraLogMessage) {
        Node savedNode = this.save(node);
        logger.info("Saved new node (" + node.getId() + ")" + node.getName() + ". " + extraLogMessage);
        return savedNode;
    }

    /*@Query(value =
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
            "SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, -generationsDown AS generationscount FROM nodes JOIN descendents ON nodes.genealogy_id = descendents.to_node_id\n" +
            "UNION SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, generationsUp AS generationscount FROM nodes JOIN ancestors ON nodes.genealogy_id = ancestors.from_node_id\n" +
            "UNION SELECT genealogy_id, lastupdated, name, numberofdescendents, scraped, 0 AS generationscount FROM nodes WHERE nodes.genealogy_id = :baseId"
            , nativeQuery = true)
    List<Node> getNodes(@Param("baseId") Integer base, @Param("maxGenerationsDown") Integer maxGenerationsDown, @Param("maxGenerationsUp") Integer maxGenerationsUp);*/

    List<Object[]> getNodesRaw(@Param("baseId") Integer base, @Param("maxGenerationsDown") Integer maxGenerationsDown, @Param("maxGenerationsUp") Integer maxGenerationsUp);
    default List<Pair<Node, Integer>> getNodes(Integer base) {
        return getNodes(base, 5, 5);
    }

    default List<Pair<Node, Integer>> getNodes(Integer base, Integer maxGenerationsDown, Integer maxGenerationsUp) {
        List<Object[]> nodesRaw = getNodesRaw(base, maxGenerationsDown, maxGenerationsUp);
        List<Pair<Node, Integer>> nodes = new ArrayList<>();
        for (Object[] nodeRaw : nodesRaw) {
            nodes.add(Pair.of((Node)nodeRaw[0], (Integer)nodeRaw[1]));
        }
        return nodes;
    }


    //Graph getGraph(Integer id, Integer maxGenerationsUp, Integer maxGenerationsDown);
}
