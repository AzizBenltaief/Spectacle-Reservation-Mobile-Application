package com.example.spectacleApplication.Controllers;

import com.example.spectacleApplication.DTO.UserDTO;
import com.example.spectacleApplication.Services.UserService;
import com.example.spectacleApplication.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public User login(@RequestParam String username, @RequestParam String password) {
        return userService.authenticate(username, password);
    }

    @PostMapping("/register")
    public User register(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

}
