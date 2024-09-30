package com.mathsgenealogyapi.node;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {

    @Transactional
    default Node getOrInsert(Node node) {
        return this.findById(node.getId())
                .orElseGet(() -> this.save(node));
    }
}
