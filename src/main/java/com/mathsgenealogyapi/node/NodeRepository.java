package com.mathsgenealogyapi.node;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
