package com.example.spectacleprojet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spectacleprojet.DTO.BilletDTO;
import com.example.spectacleprojet.DTO.ProgrammationDTO;
import com.example.spectacleprojet.DTO.ReservationDTO;
import com.example.spectacleprojet.DTO.SpectacleDetailsDTO;
import com.example.spectacleprojet.DTO.TicketTypeDTO;
import com.example.spectacleprojet.network.ApiService;
import com.example.spectacleprojet.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {
    private TextView userInfoTextView, quantityTextView, priceTextView;
    private Button plusButton, minusButton, confirmButton;
    private Spinner paymentMethodSpinner, dateSpinner;
    private RadioGroup ticketCategoryRadioGroup;
    private EditText cardCodeEditText;
    private ProgressBar loadingProgress;
    private SharedPreferences prefs;
    private int spectacleId, quantity = 1;
    private int programmationId = -1;
    private float ticketPrice = 0.0f;
    private String selectedTicketCategory = "";
    private List<ProgrammationDTO> programmationList;
    private int spectaclePlacesDisponibles;
    private SpectacleDetailsDTO spectacleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        userInfoTextView = findViewById(R.id.user_info);
        quantityTextView = findViewById(R.id.quantity_text);
        priceTextView = findViewById(R.id.price_text);
        plusButton = findViewById(R.id.plus_button);
        minusButton = findViewById(R.id.minus_button);
        paymentMethodSpinner = findViewById(R.id.payment_method_spinner);
        ticketCategoryRadioGroup = findViewById(R.id.ticket_category_radio_group);
        dateSpinner = findViewById(R.id.date_spinner);
        cardCodeEditText = findViewById(R.id.card_code_edit_text);
        confirmButton = findViewById(R.id.confirm_button);
        loadingProgress = findViewById(R.id.loading_progress);
        prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        spectacleId = getIntent().getIntExtra("spectacleId", -1);
        if (spectacleId == -1) {
            Toast.makeText(this, "Invalid Spectacle ID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String nom = prefs.getString("nom", "");
        String prenom = prefs.getString("prenom", "");
        String email = prefs.getString("email", "");
        userInfoTextView.setText("Utilisateur : " + nom + " " + prenom + "\nEmail : " + email);

        fetchSpectacleDetails();

        ArrayAdapter<CharSequence> paymentAdapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(paymentAdapter);

        quantityTextView.setText(String.valueOf(quantity));
        plusButton.setOnClickListener(v -> {
            if (quantity < spectaclePlacesDisponibles) {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
                updatePriceDisplay();
            } else {
                Toast.makeText(this, "Nombre maximum de places atteint: " + spectaclePlacesDisponibles, Toast.LENGTH_SHORT).show();
            }
        });

        minusButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
                updatePriceDisplay();
            } else {
                Toast.makeText(this, "La quantité ne peut pas être inférieure à 1", Toast.LENGTH_SHORT).show();
            }
        });

        confirmButton.setOnClickListener(v -> {
            String cardCode = cardCodeEditText.getText().toString();
            if (cardCode.isEmpty()) {
                Toast.makeText(ReservationActivity.this, "Veuillez entrer le code de la carte", Toast.LENGTH_SHORT).show();
                return;
            }

            if (programmationId == -1) {
                Toast.makeText(ReservationActivity.this, "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show();
                return;
            }

            if (quantity > spectaclePlacesDisponibles) {
                Toast.makeText(ReservationActivity.this, "Pas assez de places disponibles", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedTicketCategory.isEmpty()) {
                Toast.makeText(ReservationActivity.this, "Veuillez sélectionner une catégorie de billet", Toast.LENGTH_SHORT).show();
                return;
            }

            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setProgrammationId(programmationId);
            reservationDTO.setSpectacleId(spectacleId);
            reservationDTO.setUserId(prefs.getInt("userId", -1));
            reservationDTO.setNumberOfPlaces(quantity);
            reservationDTO.setCategorieBillet(selectedTicketCategory);
            reservationDTO.setPrix(ticketPrice * quantity);

            createReservation(reservationDTO);
        });
    }

    private void fetchSpectacleDetails() {
        loadingProgress.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<SpectacleDetailsDTO> call = apiService.getSpectacleDetails(spectacleId);
        call.enqueue(new Callback<SpectacleDetailsDTO>() {
            @Override
            public void onResponse(Call<SpectacleDetailsDTO> call, Response<SpectacleDetailsDTO> response) {
                loadingProgress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    spectacleDetails = response.body();
                    Log.d("ReservationActivity", "SpectacleDetails: " + spectacleDetails.toString());
                    programmationList = spectacleDetails.getProgrammations();
                    spectaclePlacesDisponibles = spectacleDetails.getPlacesDisponibles() != null
                            ? spectacleDetails.getPlacesDisponibles() : 0;
                    populateDateSpinner();
                    populateTicketCategories();
                } else {
                    Toast.makeText(ReservationActivity.this, "Erreur lors du chargement des détails", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpectacleDetailsDTO> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                Toast.makeText(ReservationActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateDateSpinner() {
        if (programmationList == null || programmationList.isEmpty()) {
            Toast.makeText(this, "Aucune date disponible", Toast.LENGTH_SHORT).show();
            dateSpinner.setEnabled(false);
            confirmButton.setEnabled(false);
            return;
        }

        ArrayAdapter<ProgrammationDTO> dateAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, programmationList);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProgrammationDTO selectedProgrammation = programmationList.get(position);
                programmationId = selectedProgrammation.getId();
                Log.d("ReservationActivity", "Selected programmationId: " + programmationId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                programmationId = -1;
            }
        });
    }

    private void populateTicketCategories() {
        List<TicketTypeDTO> ticketTypes = spectacleDetails.getTicketTypes();
        if (ticketTypes == null || ticketTypes.isEmpty()) {
            Toast.makeText(this, "Aucune catégorie de billet disponible", Toast.LENGTH_SHORT).show();
            confirmButton.setEnabled(false);
            return;
        }
        for (TicketTypeDTO ticketType : ticketTypes) {
            Log.d("ReservationActivity", "TicketType: " + ticketType.toString());
        }

        for (int i = 0; i < ticketTypes.size(); i++) {
            TicketTypeDTO ticketType = ticketTypes.get(i);
            String ticketName = ticketType.getName() != null ? ticketType.getName() : "Unknown";
            Float ticketPriceValue = ticketType.getPrice() != null ? ticketType.getPrice() : 0.0f;

            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(View.generateViewId());
            radioButton.setText(String.format("%s - %.2f TND", ticketName, ticketPriceValue));
            radioButton.setTextColor(getResources().getColor(android.R.color.black));
            radioButton.setTextSize(16);
            radioButton.setTag(R.id.ticket_price, ticketPriceValue);
            radioButton.setTag(R.id.ticket_name, ticketName);
            ticketCategoryRadioGroup.addView(radioButton);

            if (i == 0) {
                radioButton.setChecked(true);
                ticketPrice = ticketPriceValue;
                selectedTicketCategory = ticketName;
                updatePriceDisplay();
            }
        }

        ticketCategoryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadioButton = findViewById(checkedId);
            if (selectedRadioButton != null) {
                Float price = (Float) selectedRadioButton.getTag(R.id.ticket_price);
                String name = (String) selectedRadioButton.getTag(R.id.ticket_name);
                ticketPrice = price != null ? price : 0.0f;
                selectedTicketCategory = name != null ? name : "";
                updatePriceDisplay();
            }
        });
    }

    private void updatePriceDisplay() {
        float totalPrice = ticketPrice * quantity;
        priceTextView.setText(String.format("Prix : %.2f TND", totalPrice));
    }

    private void createReservation(ReservationDTO reservationDTO) {
        loadingProgress.setVisibility(View.VISIBLE);
        confirmButton.setEnabled(false);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        apiService.createReservation(reservationDTO).enqueue(new Callback<BilletDTO>() {
            @Override
            public void onResponse(Call<BilletDTO> call, Response<BilletDTO> response) {
                loadingProgress.setVisibility(View.GONE);
                confirmButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    BilletDTO billet = response.body();

                    String selectedDate = "";
                    String selectedTime = "";
                    for (ProgrammationDTO programmation : programmationList) {
                        if (programmation.getId() == programmationId) {
                            selectedDate = programmation.getDate();
                            selectedTime = programmation.getHeure();
                            break;
                        }
                    }

                    Toast.makeText(ReservationActivity.this, "Réservation confirmée pour " + billet.getSpectacleTitre() + ", Billet ID: " + billet.getId(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ReservationActivity.this, ConfirmationActivity.class);
                    intent.putExtra("spectacleTitre", billet.getSpectacleTitre());
                    intent.putExtra("date", selectedDate);
                    intent.putExtra("heure", selectedTime);
                    intent.putExtra("numberOfPlaces", reservationDTO.getNumberOfPlaces());
                    intent.putExtra("categorieBillet", reservationDTO.getCategorieBillet());
                    intent.putExtra("prix", reservationDTO.getPrix());
                    intent.putExtra("billetId", billet.getId());
                    startActivity(intent);
                    finish();
                } else {
                    String errorMessage = "Erreur lors de la réservation";
                    if (response.code() == 400) {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "";
                            if (errorBody.contains("Not enough available seats")) {
                                errorMessage = "Pas assez de places disponibles pour ce spectacle.";
                            } else if (errorBody.contains("Programmation not found")) {
                                errorMessage = "La date sélectionnée n'est plus disponible.";
                            } else if (errorBody.contains("User not found")) {
                                errorMessage = "Utilisateur non trouvé. Veuillez vous reconnecter.";
                            }
                        } catch (Exception e) {
                            Log.e("ReservationActivity", "Failed to parse error body", e);
                        }
                    }
                    Log.e("ReservationActivity", "Error response: " + response.code() + " " + response.message());
                    Toast.makeText(ReservationActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BilletDTO> call, Throwable t) {
                loadingProgress.setVisibility(View.GONE);
                confirmButton.setEnabled(true);
                Log.e("ReservationActivity", "Network failure: " + t.getMessage(), t);
                Toast.makeText(ReservationActivity.this, "Erreur de connexion: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}