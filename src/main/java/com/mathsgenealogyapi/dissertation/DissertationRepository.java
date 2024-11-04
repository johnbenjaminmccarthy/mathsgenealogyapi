package com.mathsgenealogyapi.dissertation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DissertationRepository extends JpaRepository<Dissertation, Integer> {

    @Query("SELECT d FROM Dissertation d WHERE d.node.id = :id")
    List<Dissertation> dissertationsFromNodeId(Integer id);


    @Modifying
    @Query("DELETE FROM Dissertation d WHERE d.node.id = :id")
    void deleteDissertationsWithNodeId(Integer id);
}
