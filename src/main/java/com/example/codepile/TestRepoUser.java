package com.example.codepile;

import com.example.codepile.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestRepoUser extends JpaRepository<User, UUID> {
}
