package com.example.mathsgenealogyapi.dissertation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DissertationRepository extends JpaRepository<Dissertation, Integer> {
}
