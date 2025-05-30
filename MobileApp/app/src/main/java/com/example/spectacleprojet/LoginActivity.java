package com.example.spectacleprojet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spectacleprojet.DTO.User;
import com.example.spectacleprojet.DTO.UserDTO;
import com.example.spectacleprojet.network.ApiService;
import com.example.spectacleprojet.network.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        if (prefs.getBoolean("isLoggedIn", false)) {
            redirectAfterLogin();
            return;
        }

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            login(username, password);
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String username, String password) {
        loginButton.setEnabled(false);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.login(username, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                loginButton.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putInt("userId", user.getId());
                    editor.putString("nom", user.getNom());
                    editor.putString("prenom", user.getPrenom());
                    editor.putString("email", user.getEmail());
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    redirectAfterLogin();
                } else {
                    Log.e("LoginActivity", "Error response: " + response.code() + " " + response.message());
                    try {
                        Log.e("LoginActivity", "Error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("LoginActivity", "Failed to parse error body", e);
                    }
                    Toast.makeText(LoginActivity.this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loginButton.setEnabled(true);
                Log.e("LoginActivity", "Network failure: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redirectAfterLogin() {
        int spectacleId = getIntent().getIntExtra("spectacleId", -1);
        Intent intent;
        if (spectacleId != -1) {
            intent = new Intent(LoginActivity.this, ReservationActivity.class);
            intent.putExtra("spectacleId", spectacleId);
        } else {
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}