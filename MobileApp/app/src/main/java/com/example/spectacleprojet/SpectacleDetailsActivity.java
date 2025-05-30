package com.example.spectacleprojet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.spectacleprojet.Adapter.ArtistAdapter;
import com.example.spectacleprojet.Adapter.DateAdapter;
import com.example.spectacleprojet.DTO.ArtisteDTO;
import com.example.spectacleprojet.DTO.ProgrammationDTO;
import com.example.spectacleprojet.DTO.SpectacleDetailsDTO;
import com.example.spectacleprojet.network.ApiService;
import com.example.spectacleprojet.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpectacleDetailsActivity extends AppCompatActivity {
    private ImageView imageView, Chair;
    private TextView titleTextView, descriptionTextView, lieuTextView, placesTextView, categoriesTextView, durationTextView;
    private RecyclerView datesRecyclerView, artistesRecyclerView;
    private MaterialButton reserveButton;
    private SharedPreferences prefs;
    private SpectacleDetailsDTO spectacleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectacle_details);

        imageView = findViewById(R.id.spectacle_image);
        titleTextView = findViewById(R.id.spectacle_title);
        descriptionTextView = findViewById(R.id.spectacle_description);
        lieuTextView = findViewById(R.id.spectacle_lieu);
        placesTextView = findViewById(R.id.spectacle_places);
        categoriesTextView = findViewById(R.id.categories_text);
        durationTextView = findViewById(R.id.spectacle_duration);
        datesRecyclerView = findViewById(R.id.dates_list);
        artistesRecyclerView = findViewById(R.id.artistes_list);
        reserveButton = findViewById(R.id.reserve_button);
        Chair = findViewById(R.id.chair);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        datesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int spectacleId = getIntent().getIntExtra("spectacleId", -1);
        String lieuNom = getIntent().getStringExtra("lieuNom");
        String ville = getIntent().getStringExtra("ville");

        if (lieuTextView != null) {
            if (lieuNom != null && ville != null) {
                lieuTextView.setText(lieuNom + ", " + ville);
            } else {
                lieuTextView.setText("Lieu non disponible");
            }
        } else {
            Log.e("SpectacleDetails", "lieuTextView is null - check layout for ID spectacle_lieu");
        }

        if (lieuTextView != null) {
            lieuTextView.setOnClickListener(v -> {
                if (spectacleDetails != null && spectacleDetails.getGoogleMapsLink() != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(spectacleDetails.getGoogleMapsLink()));
                    startActivity(intent);
                } else {
                    Toast.makeText(SpectacleDetailsActivity.this, "Lien Google Maps non disponible", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (reserveButton != null) {
            reserveButton.setEnabled(false);
            reserveButton.setOnClickListener(v -> {
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
                Intent intent = new Intent(SpectacleDetailsActivity.this,
                        isLoggedIn ? ReservationActivity.class : LoginActivity.class);
                intent.putExtra("spectacleId", spectacleId);
                startActivity(intent);
            });
        }

        loadSpectacleDetails(spectacleId);
    }

    private void loadSpectacleDetails(int id) {


        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getSpectacleDetails(id).enqueue(new Callback<SpectacleDetailsDTO>() {
            @Override
            public void onResponse(Call<SpectacleDetailsDTO> call, Response<SpectacleDetailsDTO> response) {
                Log.d("SpectacleDetailsActivity", "API response received, code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    spectacleDetails = response.body();
                    Log.d("SpectacleDetailsActivity", "SpectacleDetails: " + spectacleDetails.toString());

                    if (titleTextView != null) {
                        titleTextView.setText(spectacleDetails.getTitre() != null ? spectacleDetails.getTitre() : "Titre non disponible");
                    }
                    if (descriptionTextView != null) {
                        descriptionTextView.setText(spectacleDetails.getDescription() != null ? spectacleDetails.getDescription() : "Description non disponible");
                    }
                    if (placesTextView != null) {
                        Integer placesDisponibles = spectacleDetails.getPlacesDisponibles();
                        placesTextView.setText("Places disponibles : " + (placesDisponibles != null ? placesDisponibles : "N/A"));
                        if (placesDisponibles != null && placesDisponibles > 0) {
                            placesTextView.setTextColor(Color.GREEN);
                            Chair.setColorFilter(ContextCompat.getColor(SpectacleDetailsActivity.this, R.color.green_500));
                        } else {
                            placesTextView.setTextColor(Color.RED);
                            Chair.setColorFilter(ContextCompat.getColor(SpectacleDetailsActivity.this, R.color.red_500));
                        }
                    }
                    if (durationTextView != null) {
                        Float duree = spectacleDetails.getDuree();
                        if (duree != null) {
                            durationTextView.setText("Duration: " + duree + " hours");
                        } else {
                            durationTextView.setText("Duration: N/A");
                        }
                    }
                    if (reserveButton != null) {
                        boolean hasPlaces = spectacleDetails.getPlacesDisponibles() != null && spectacleDetails.getPlacesDisponibles() > 0;
                        reserveButton.setEnabled(hasPlaces);
                        if (!hasPlaces) {
                            Toast.makeText(SpectacleDetailsActivity.this, "Réservation désactivée : Aucune place disponible", Toast.LENGTH_LONG).show();
                        }
                    }

                    List<ProgrammationDTO> programmations = spectacleDetails.getProgrammations() != null ? spectacleDetails.getProgrammations() : new ArrayList<>();
                    DateAdapter datesAdapter = new DateAdapter(SpectacleDetailsActivity.this, programmations);
                    datesRecyclerView.setAdapter(datesAdapter);

                    List<ArtisteDTO> artistes = spectacleDetails.getArtistes() != null ? spectacleDetails.getArtistes() : new ArrayList<>();
                    ArtistAdapter artistesAdapter = new ArtistAdapter(SpectacleDetailsActivity.this, artistes);
                    artistesRecyclerView.setAdapter(artistesAdapter);

                    if (categoriesTextView != null) {
                        List<String> categorieNoms = spectacleDetails.getCategorieNoms() != null && !spectacleDetails.getCategorieNoms().isEmpty()
                                ? spectacleDetails.getCategorieNoms()
                                : List.of("Inconnu");
                        String categoriesString = String.join(", ", categorieNoms);
                        categoriesTextView.setText(categoriesString);
                        Log.d("SpectacleDetailsActivity", "Displaying categories: " + categoriesString);
                    }

                    Log.d("SpectacleDetailsActivity", "imageView: " + (imageView != null ? "not null" : "null"));
                    Log.d("SpectacleDetailsActivity", "spectacleDetails.getImageURL(): " + (spectacleDetails.getImageURL() != null ? spectacleDetails.getImageURL() : "null"));

                    if (imageView != null && spectacleDetails.getImageURL() != null) {
                        String imageUrl = spectacleDetails.getImageURL();
                        Log.d("SpectacleDetailsActivity", "Attempting to load image from URL: " + imageUrl);
                        System.out.println("Image URL: " + imageUrl);

                        imageView.setVisibility(View.VISIBLE);

                        Glide.with(SpectacleDetailsActivity.this)
                                .load(imageUrl)
                                .thumbnail(0.25f)
                                .override(500, 500)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.placeholder_image)
                                .error(R.drawable.error_image)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        Log.e("SpectacleDetailsActivity", "Failed to load image: " + (e != null ? e.getMessage() : "Unknown error"));
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        Log.d("SpectacleDetailsActivity", "Image loaded successfully from " + dataSource);
                                        return false;
                                    }
                                })
                                .into(imageView);
                    } else {
                        Log.e("SpectacleDetailsActivity", "Cannot load image: imageView or imageURL is null");
                        if (imageView != null) {
                            imageView.setImageResource(R.drawable.placeholder_image);
                        }
                    }
                } else {
                    Log.e("SpectacleDetailsActivity", "API response unsuccessful or body is null, code: " + response.code());
                    Toast.makeText(SpectacleDetailsActivity.this, "Erreur lors du chargement des détails", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SpectacleDetailsDTO> call, Throwable t) {
                Log.e("SpectacleDetailsActivity", "Network failure: " + t.getMessage(), t);
                Toast.makeText(SpectacleDetailsActivity.this, "Erreur de chargement : " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setResult(RESULT_OK);
    }
}