package com.example.spectacleprojet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleprojet.Adapter.BilletAdapter;
import com.example.spectacleprojet.DTO.BilletResponseDTO;
import com.example.spectacleprojet.network.ApiService;
import com.example.spectacleprojet.network.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MesReservations extends AppCompatActivity implements BilletAdapter.OnCancelClickListener {
    private RecyclerView recyclerView;
    private ProgressBar loadingProgress;
    private BilletAdapter adapter;
    private SharedPreferences prefs;
    private List<BilletResponseDTO> billetDetailsList;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_reservations);

        // Set up the ActionBar with "Up" button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mes Réservations");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(getResources().getColor(R.color.background_grey)));
        }

        recyclerView = findViewById(R.id.recyclerView);
        loadingProgress = findViewById(R.id.loading_progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        billetDetailsList = new ArrayList<>();
        adapter = new BilletAdapter(billetDetailsList, this);
        recyclerView.setAdapter(adapter);

        loadBilletDetails();
    }

    private void loadBilletDetails() {
        if (isLoading) return;
        loadingProgress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        isLoading = true;
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            loadingProgress.setVisibility(View.GONE);
            isLoading = false;
            Toast.makeText(this, "Erreur: Utilisateur non connecté", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MesReservations.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getBilletDetailsByUserId(userId).enqueue(new Callback<List<BilletResponseDTO>>() {
            @Override
            public void onResponse(Call<List<BilletResponseDTO>> call, Response<List<BilletResponseDTO>> response) {
                loadingProgress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                isLoading = false;
                if (response.isSuccessful() && response.body() != null) {
                    billetDetailsList = response.body();
                    if (billetDetailsList.isEmpty()) {
                        Toast.makeText(MesReservations.this, "Aucune réservation trouvée", Toast.LENGTH_LONG).show();
                    }
                    adapter.updateList(billetDetailsList);
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(MesReservations.this, "Erreur: " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                        Log.e("MesReservationsActivity", "Response error: " + response.code() + " - " + errorBody);
                    } catch (IOException e) {
                        Log.e("MesReservationsActivity", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BilletResponseDTO>> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                isLoading = false;
                Toast.makeText(MesReservations.this, "Erreur de chargement: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("MesReservationsActivity", "Network failure: ", t);
            }
        });
    }

    @Override
    public void onCancelClick(int billetId, int position) {
        if (isLoading) {
            Toast.makeText(this, "Une opération est en cours, veuillez patienter", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmer l'annulation")
                .setMessage("Voulez-vous vraiment annuler ce billet ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    cancelBillet(billetId);
                })
                .setNegativeButton("Non", null)
                .show();
    }

    private void cancelBillet(int billetId) {
        isLoading = true;
        loadingProgress.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.cancelBillet(billetId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loadingProgress.setVisibility(View.GONE);
                isLoading = false;
                if (response.isSuccessful()) {
                    Toast.makeText(MesReservations.this, "Réservation annulée avec succès", Toast.LENGTH_SHORT).show();
                    loadBilletDetails();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(MesReservations.this, "Erreur lors de l'annulation: " + errorBody, Toast.LENGTH_LONG).show();
                        Log.e("MesReservationsActivity", "Cancellation error: " + response.code() + " - " + errorBody);
                    } catch (IOException e) {
                        Log.e("MesReservationsActivity", "Error parsing error body", e);
                    }
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                isLoading = false;
                Toast.makeText(MesReservations.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("MesReservationsActivity", "Network failure during cancellation: ", t);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}