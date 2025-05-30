package com.example.spectacleprojet;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spectacleprojet.Adapter.SpectacleAdapter;
import com.example.spectacleprojet.DTO.SpectacleDTO;
import com.example.spectacleprojet.DTO.SpectacleDetailsDTO;
import com.example.spectacleprojet.DTO.ProgrammationDTO;
import com.example.spectacleprojet.network.ApiService;
import com.example.spectacleprojet.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.DatePickerDialog;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar loadingProgress;
    private SpectacleAdapter adapter;
    private Button logoutButton, filterByDateButton, filterByLieuButton, resetFiltersButton;
    private SharedPreferences prefs;
    private List<SpectacleDTO> spectacleList;
    private List<SpectacleDetailsDTO> spectacleDetailsList;
    private List<SpectacleDTO> filteredList = new ArrayList<>();
    private Map<Integer, List<ProgrammationDTO>> spectacleProgrammations = new HashMap<>();
    private androidx.appcompat.widget.SearchView searchView;
    private LocalDate selectedFilterDate;
    private String selectedLieu;
    private Button mesReservationsButton;
    private static final int REQUEST_CODE_SPECTACLE_DETAILS = 1;
    private static final int MAX_RETRIES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        loadingProgress = findViewById(R.id.loading_progress);
        logoutButton = findViewById(R.id.logout_button);
        filterByDateButton = findViewById(R.id.filter_by_date_button);
        filterByLieuButton = findViewById(R.id.filter_by_lieu_button);
        resetFiltersButton = findViewById(R.id.reset_filters_button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        mesReservationsButton = findViewById(R.id.mes_reservations_button);
        searchView = findViewById(R.id.search_view);
        SearchView searchView = findViewById(R.id.search_view);

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ImageView closeIcon = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        searchIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        closeIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        androidx.appcompat.widget.SearchView.SearchAutoComplete searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTextColor(Color.WHITE);
        searchText.setHintTextColor(Color.parseColor("#80FFFFFF"));
        setupSearchView();

        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        logoutButton.setEnabled(isLoggedIn);
        mesReservationsButton.setEnabled(isLoggedIn);

        mesReservationsButton.setOnClickListener(v -> {
            if (isLoggedIn) {
                navigateToMesReservations();
            } else {
                Toast.makeText(MainActivity.this, "Veuillez vous connecter pour voir vos réservations", Toast.LENGTH_SHORT).show();
            }
        });

        if (spectacleList == null) {
            spectacleList = new ArrayList<>();
        }
        if (spectacleDetailsList == null) {
            spectacleDetailsList = new ArrayList<>();
        }

        adapter = new SpectacleAdapter(spectacleList, spectacleDetailsList, (spectacle, spectacleDetails) -> {
            Intent intent = new Intent(MainActivity.this, SpectacleDetailsActivity.class);
            intent.putExtra("spectacleId", spectacle.getId());
            intent.putExtra("lieuNom", spectacle.getLieuNom());
            intent.putExtra("ville", spectacle.getVille());
            intent.putExtra("imageUrl", spectacle.getImageURL());
            startActivityForResult(intent, REQUEST_CODE_SPECTACLE_DETAILS);
        });
        recyclerView.setAdapter(adapter);

        loadSpectacles();

        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());

        filterByDateButton.setOnClickListener(v -> showDatePickerDialog());
        filterByLieuButton.setOnClickListener(v -> showLieuSelectionDialog());

        if (resetFiltersButton != null) {
            resetFiltersButton.setOnClickListener(v -> resetFilters());
        } else {
            Log.e("MainActivity", "resetFiltersButton is null. Ensure the button with ID 'reset_filters_button' exists in activity_main.xml");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPECTACLE_DETAILS && resultCode == RESULT_OK) {
            loadSpectacles();
        }
    }

    private void resetFilters() {
        searchView.setQuery("", false);
        searchView.clearFocus();
        selectedFilterDate = null;
        selectedLieu = null;
        filteredList.clear();
        filteredList.addAll(spectacleList);
        adapter.updateLists(filteredList, spectacleDetailsList);
        Toast.makeText(this, "Filtres réinitialisés", Toast.LENGTH_SHORT).show();
    }

    private void loadSpectacles() {
        loadingProgress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getAllSpectacles().enqueue(new Callback<List<SpectacleDTO>>() {
            @Override
            public void onResponse(Call<List<SpectacleDTO>> call, Response<List<SpectacleDTO>> response) {
                if (response.isSuccessful()) {
                    if (spectacleList == null) {
                        spectacleList = new ArrayList<>();
                    }
                    spectacleList.clear();
                    spectacleList.addAll(response.body());
                    filteredList.clear();
                    filteredList.addAll(spectacleList);
                    fetchSpectacleDetails(apiService);
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(MainActivity.this, "Erreur: " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                        Log.e("MainActivity", "Response error: " + response.code() + " - " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loadingProgress.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<SpectacleDTO>> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, "Erreur de chargement: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("MainActivity", "Network failure: ", t);
            }
        });
    }

    private void navigateToMesReservations() {
        Intent intent = new Intent(MainActivity.this, MesReservations.class);
        startActivity(intent);
    }

    private LocalDate getEarliestDate(List<ProgrammationDTO> programmations) {
        if (programmations == null || programmations.isEmpty()) {
            return null;
        }
        LocalDate earliestDate = null;
        for (ProgrammationDTO programmation : programmations) {
            String dateStr = programmation.getDate();
            if (dateStr != null) {
                try {
                    LocalDate date = LocalDate.parse(dateStr);
                    if (earliestDate == null || date.isBefore(earliestDate)) {
                        earliestDate = date;
                    }
                } catch (Exception e) {
                    Log.e("MainActivity", "Error parsing date: " + dateStr, e);
                }
            }
        }
        return earliestDate;
    }

    private void sortSpectaclesByNearestDate() {
        if (spectacleList == null || spectacleProgrammations == null) {
            return;
        }

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < spectacleList.size(); i++) {
            indices.add(i);
        }

        Collections.sort(indices, (i1, i2) -> {
            SpectacleDTO spectacle1 = spectacleList.get(i1);
            SpectacleDTO spectacle2 = spectacleList.get(i2);
            LocalDate date1 = getEarliestDate(spectacleProgrammations.get(spectacle1.getId()));
            LocalDate date2 = getEarliestDate(spectacleProgrammations.get(spectacle2.getId()));

            if (date1 == null && date2 == null) return 0;
            if (date1 == null) return 1;
            if (date2 == null) return -1;
            return date1.compareTo(date2);
        });

        List<SpectacleDTO> sortedSpectacleList = new ArrayList<>();
        List<SpectacleDetailsDTO> sortedDetailsList = new ArrayList<>();
        for (int index : indices) {
            sortedSpectacleList.add(spectacleList.get(index));
            sortedDetailsList.add(spectacleDetailsList.get(index));
        }

        spectacleList.clear();
        spectacleList.addAll(sortedSpectacleList);
        spectacleDetailsList.clear();
        spectacleDetailsList.addAll(sortedDetailsList);

        filteredList.clear();
        filteredList.addAll(spectacleList);
    }

    private void fetchSpectacleDetails(ApiService apiService) {
        spectacleProgrammations.clear();
        spectacleDetailsList.clear();
        if (spectacleList.isEmpty()) {
            loadingProgress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Aucun spectacle disponible", Toast.LENGTH_LONG).show();
            adapter.updateLists(filteredList, spectacleDetailsList);
            return;
        }

        for (int i = 0; i < spectacleList.size(); i++) {
            spectacleDetailsList.add(null);
        }

        final int[] completedRequests = {0};
        final int totalRequests = spectacleList.size();
        final int[] failedRequests = {0};

        for (int i = 0; i < spectacleList.size(); i++) {
            final int position = i;
            SpectacleDTO spectacle = spectacleList.get(i);
            fetchSpectacleDetailsWithRetry(apiService, spectacle, 0, new Callback<SpectacleDetailsDTO>() {
                @Override
                public void onResponse(Call<SpectacleDetailsDTO> call, Response<SpectacleDetailsDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        SpectacleDetailsDTO details = response.body();
                        spectacleDetailsList.set(position, details);
                        List<ProgrammationDTO> programmations = details.getProgrammations();
                        spectacleProgrammations.put(spectacle.getId(), programmations != null ? programmations : new ArrayList<>());
                        Log.d("MainActivity", "Successfully loaded details for spectacle ID: " + spectacle.getId() + ", placesDisponibles: " + details.getPlacesDisponibles());
                    } else {
                        failedRequests[0]++;
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Log.e("MainActivity", "Failed to load details for spectacle ID: " + spectacle.getId() + ", HTTP Code: " + response.code() + ", Error: " + errorBody);
                        } catch (IOException e) {
                            Log.e("MainActivity", "Error reading error body for spectacle ID: " + spectacle.getId(), e);
                        }
                    }

                    completedRequests[0]++;
                    if (completedRequests[0] == totalRequests) {
                        if (failedRequests[0] > 0) {
                            Toast.makeText(MainActivity.this, failedRequests[0] + " spectacle(s) n'ont pas pu être chargés complètement", Toast.LENGTH_LONG).show();
                        }
                        sortSpectaclesByNearestDate();
                        loadingProgress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.updateLists(filteredList, spectacleDetailsList);
                        filterSpectacles(searchView.getQuery().toString());
                    }
                }

                @Override
                public void onFailure(Call<SpectacleDetailsDTO> call, Throwable t) {
                    failedRequests[0]++;
                    Log.e("MainActivity", "Network failure for spectacle ID: " + spectacle.getId() + ", Error: " + t.getMessage(), t);

                    completedRequests[0]++;
                    if (completedRequests[0] == totalRequests) {
                        if (failedRequests[0] > 0) {
                            Toast.makeText(MainActivity.this, failedRequests[0] + " spectacle(s) n'ont pas pu être chargés complètement", Toast.LENGTH_LONG).show();
                        }
                        sortSpectaclesByNearestDate();
                        loadingProgress.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.updateLists(filteredList, spectacleDetailsList);
                        filterSpectacles(searchView.getQuery().toString());
                    }
                }
            });
        }
    }

    private void fetchSpectacleDetailsWithRetry(ApiService apiService, SpectacleDTO spectacle, int retryCount, Callback<SpectacleDetailsDTO> callback) {
        apiService.getSpectacleDetails(spectacle.getId()).enqueue(new Callback<SpectacleDetailsDTO>() {
            @Override
            public void onResponse(Call<SpectacleDetailsDTO> call, Response<SpectacleDetailsDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResponse(call, response);
                } else if (retryCount < MAX_RETRIES) {
                    Log.w("MainActivity", "Retrying fetch for spectacle ID: " + spectacle.getId() + ", Attempt: " + (retryCount + 1));
                    fetchSpectacleDetailsWithRetry(apiService, spectacle, retryCount + 1, callback);
                } else {
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<SpectacleDetailsDTO> call, Throwable t) {
                if (retryCount < MAX_RETRIES) {
                    Log.w("MainActivity", "Retrying fetch for spectacle ID: " + spectacle.getId() + ", Attempt: " + (retryCount + 1));
                    fetchSpectacleDetailsWithRetry(apiService, spectacle, retryCount + 1, callback);
                } else {
                    callback.onFailure(call, t);
                }
            }
        });
    }

    private void showDatePickerDialog() {
        LocalDate today = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedFilterDate = LocalDate.of(year, month + 1, dayOfMonth);
                    filterSpectaclesByDate(selectedFilterDate);
                },
                today.getYear(),
                today.getMonthValue() - 1,
                today.getDayOfMonth()
        );
        datePickerDialog.show();
    }

    private void filterSpectaclesByDate(LocalDate selectedFilterDate) {
        if (spectacleList == null || spectacleList.isEmpty()) {
            Toast.makeText(this, "Aucune donnée disponible pour filtrer", Toast.LENGTH_SHORT).show();
            return;
        }

        filteredList.clear();
        for (SpectacleDTO spectacle : spectacleList) {
            List<ProgrammationDTO> programmations = spectacleProgrammations.get(spectacle.getId());
            if (programmations != null) {
                for (ProgrammationDTO programmation : programmations) {
                    String dateStr = programmation.getDate();
                    if (dateStr != null) {
                        try {
                            LocalDate programmeDate = LocalDate.parse(dateStr);
                            if (programmeDate.equals(selectedFilterDate)) {
                                filteredList.add(spectacle);
                                break;
                            }
                        } catch (Exception e) {
                            Log.e("MainActivity", "Error parsing date: " + dateStr, e);
                        }
                    }
                }
            }
        }

        adapter.updateLists(filteredList, spectacleDetailsList);
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Aucun spectacle trouvé pour le " + selectedFilterDate, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Spectacles filtrés pour le " + selectedFilterDate, Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation de déconnexion")
                .setMessage("Êtes-vous sûr de vouloir vous déconnecter ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();
                    logoutButton.setEnabled(false);
                    Toast.makeText(MainActivity.this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LogoutActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Non", (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .show();
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSpectacles(newText);
                return true;
            }
        });
    }

    private void filterSpectacles(String query) {
        if (spectacleList == null || spectacleList.isEmpty()) {
            Log.d("FilterSpectacles", "Spectacle list is null or empty");
            return;
        }

        List<SpectacleDTO> tempFilteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase(Locale.getDefault()).trim();

        if (lowerCaseQuery.isEmpty()) {
            tempFilteredList.addAll(spectacleList);
            Log.d("FilterSpectacles", "No search query, starting with all spectacles: " + tempFilteredList.size());
        } else {
            for (SpectacleDTO spectacle : spectacleList) {
                if (spectacle.getTitre().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        spectacle.getLieuNom().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery) ||
                        spectacle.getVille().toLowerCase(Locale.getDefault()).contains(lowerCaseQuery)) {
                    tempFilteredList.add(spectacle);
                }
            }
            Log.d("FilterSpectacles", "After search query filter: " + tempFilteredList.size());
        }

        if (selectedFilterDate != null) {
            List<SpectacleDTO> dateFilteredList = new ArrayList<>();
            for (SpectacleDTO spectacle : tempFilteredList) {
                List<ProgrammationDTO> programmations = spectacleProgrammations.get(spectacle.getId());
                if (programmations != null) {
                    for (ProgrammationDTO programmation : programmations) {
                        String dateStr = programmation.getDate();
                        if (dateStr != null) {
                            try {
                                LocalDate programmeDate = LocalDate.parse(dateStr);
                                if (programmeDate.equals(selectedFilterDate)) {
                                    dateFilteredList.add(spectacle);
                                    break;
                                }
                            } catch (Exception e) {
                                Log.e("FilterSpectacles", "Error parsing date: " + dateStr, e);
                            }
                        }
                    }
                }
            }
            tempFilteredList = dateFilteredList;
            Log.d("FilterSpectacles", "After date filter: " + tempFilteredList.size());
        }

        if (selectedLieu != null) {
            Log.d("FilterSpectacles", "Filtering by lieu: " + selectedLieu);
            List<SpectacleDTO> lieuFilteredList = new ArrayList<>();
            for (SpectacleDTO spectacle : tempFilteredList) {
                String lieuNom = spectacle.getLieuNom();
                Log.d("FilterSpectacles", "Spectacle ID " + spectacle.getId() + " lieuNom: " + lieuNom);
                if (lieuNom != null && lieuNom.equalsIgnoreCase(selectedLieu)) {
                    lieuFilteredList.add(spectacle);
                    Log.d("FilterSpectacles", "Match found for spectacle ID " + spectacle.getId() + " at lieu: " + lieuNom);
                }
            }
            tempFilteredList = lieuFilteredList;
            Log.d("FilterSpectacles", "After lieu filter: " + tempFilteredList.size());
        }

        filteredList = tempFilteredList;
        adapter.updateLists(filteredList, spectacleDetailsList);
        if (tempFilteredList.isEmpty() && (!lowerCaseQuery.isEmpty() || selectedFilterDate != null || selectedLieu != null)) {
            Toast.makeText(this, "Aucun spectacle trouvé", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLieuSelectionDialog() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.getAllLieux().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> lieux = response.body();
                    Log.d("MainActivity", "Nombre de lieux récupérés : " + lieux.size());
                    for (String lieu : lieux) {
                        Log.d("MainActivity", "Lieu : " + lieu);
                    }

                    if (lieux.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Aucun lieu disponible", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Collections.sort(lieux);

                    String[] lieuxArray = lieux.toArray(new String[0]);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Sélectionner un lieu")
                            .setSingleChoiceItems(lieuxArray, -1, (dialog, which) -> {
                                selectedLieu = lieuxArray[which];
                                Toast.makeText(MainActivity.this, "Lieu sélectionné: " + selectedLieu, Toast.LENGTH_SHORT).show();
                                filterSpectacles(searchView.getQuery().toString());
                                dialog.dismiss();
                            })
                            .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                            .setCancelable(true)
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors de la récupération des lieux", Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Log.e("MainActivity", "Erreur API, code: " + response.code() + ", message: " + errorBody);
                    } catch (Exception e) {
                        Log.e("MainActivity", "Erreur lors de la lecture du corps d'erreur", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Échec de la requête réseau : " + t.getMessage(), t);
            }
        });
    }}