package com.example.codepile.data.repositories;

import com.example.codepile.data.entities.Pile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PileRepository extends JpaRepository<Pile, String> {
    boolean existsPileById(String id);
    Pile findPileById(String id);
}
