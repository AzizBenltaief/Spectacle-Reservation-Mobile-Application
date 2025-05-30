package com.example.spectacleApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO { private String nom; private String prenom; private String email; private String password; private Integer numTel; private String username; }
