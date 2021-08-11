package com.example.codepile;

import com.example.codepile.data.entities.Pile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepoPile extends JpaRepository<Pile, Long> {
}
