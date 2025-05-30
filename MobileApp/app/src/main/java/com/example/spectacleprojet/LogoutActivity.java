package com.example.spectacleprojet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LogoutActivity extends AppCompatActivity {
    private static final String PREF_NAME = "UserPrefs";
    private static final String TAG = "LogoutActivity";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        Log.d(TAG, "Starting logout process");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Log.d(TAG, "Logout completed");

            Toast.makeText(LogoutActivity.this, "Déconnexion faite avec succès", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LogoutActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}