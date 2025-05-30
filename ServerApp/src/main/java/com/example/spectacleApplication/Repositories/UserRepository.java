package com.example.spectacleApplication.Repositories;

import com.example.spectacleApplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> { Optional findByUsername(String username); }
