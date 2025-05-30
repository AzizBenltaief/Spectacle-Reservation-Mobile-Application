package com.example.spectacleprojet;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleprojet.DTO.User;
import com.example.spectacleprojet.DTO.UserDTO;
import com.example.spectacleprojet.network.ApiService;
import com.example.spectacleprojet.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText nomEditText, prenomEditText, emailEditText, passwordEditText, numTelEditText, usernameEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nomEditText = findViewById(R.id.nom_edit_text);
        prenomEditText = findViewById(R.id.prenom_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        numTelEditText = findViewById(R.id.num_tel_edit_text);
        usernameEditText = findViewById(R.id.username_edit_text);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(v -> {
            String nom = nomEditText.getText().toString();
            String prenom = prenomEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String numTelStr = numTelEditText.getText().toString();
            String username = usernameEditText.getText().toString();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || numTelStr.isEmpty() || username.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            UserDTO userDTO = new UserDTO();
            userDTO.setNom(nom);
            userDTO.setPrenom(prenom);
            userDTO.setEmail(email);
            userDTO.setPassword(password);
            userDTO.setNumTel(Integer.parseInt(numTelStr));
            userDTO.setUsername(username);

            register(userDTO);
        });
    }

    private void register(UserDTO userDTO) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.register(userDTO).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Erreur d'inscription", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }
}