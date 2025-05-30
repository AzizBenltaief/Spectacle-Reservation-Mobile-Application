package com.example.spectacleApplication.Services;

import com.example.spectacleApplication.DTO.UserDTO;
import com.example.spectacleApplication.Repositories.UserRepository;
import com.example.spectacleApplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        }
        throw new RuntimeException("Invalid credentials");
    }

    public User createUser(UserDTO userDTO) {
        // Check if username already exists
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setNom(userDTO.getNom());
        user.setPrenom(userDTO.getPrenom());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // In a real app, hash the password
        user.setNumTel(userDTO.getNumTel());
        user.setUsername(userDTO.getUsername());

        return userRepository.save(user);
    }
}
