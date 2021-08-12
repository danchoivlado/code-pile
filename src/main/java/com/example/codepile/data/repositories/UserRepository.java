package com.example.codepile.data.repositories;

import com.example.codepile.data.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsername(String username);
}
