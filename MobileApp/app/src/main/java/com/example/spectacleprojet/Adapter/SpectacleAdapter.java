package com.example.spectacleprojet.Adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.spectacleprojet.DTO.SpectacleDTO;
import com.example.spectacleprojet.DTO.SpectacleDetailsDTO;
import com.example.spectacleprojet.R;
import com.example.spectacleprojet.network.ApiService;
import com.example.spectacleprojet.network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SpectacleAdapter extends RecyclerView.Adapter<SpectacleAdapter.ViewHolder> {
    private List<SpectacleDTO> spectacleList; // List for SpectacleDTO (title, lieuNom, ville)
    private List<SpectacleDetailsDTO> spectacleDetailsList;
    private OnSpectacleClickListener listener;
    private ApiService apiService;

    public interface OnSpectacleClickListener {
        void onSpectacleClick(SpectacleDTO spectacle, SpectacleDetailsDTO spectacleDetails);
    }

    public SpectacleAdapter(List<SpectacleDTO> spectacleList, List<SpectacleDetailsDTO> spectacleDetailsList, OnSpectacleClickListener listener) {
        this.spectacleList = spectacleList;
        this.spectacleDetailsList = spectacleDetailsList;
        this.listener = listener;
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spectacle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SpectacleDTO spectacle = spectacleList.get(position);
        holder.titleTextView.setText(spectacle.getTitre());
        holder.lieuTextView.setText(spectacle.getLieuNom() + ", " + spectacle.getVille());

        // Get the SpectacleDetailsDTO for detailed info (placesDisponibles)
        SpectacleDetailsDTO spectacleDetails = (spectacleDetailsList != null && position < spectacleDetailsList.size())
                ? spectacleDetailsList.get(position)
                : null;

        if (spectacleDetails != null) {
            bindPlacesDisponibles(holder, spectacleDetails);
        } else {
            holder.placeDisponible.setText("Chargement...");
            holder.placeDisponible.setTextColor(Color.GRAY);
            holder.placeDisponible.setCompoundDrawableTintList(ColorStateList.valueOf(Color.GRAY)); // Teinte grise pour l'icône pendant le chargement

            apiService.getSpectacleDetails(spectacle.getId()).enqueue(new Callback<SpectacleDetailsDTO>() {
                @Override
                public void onResponse(Call<SpectacleDetailsDTO> call, Response<SpectacleDetailsDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        SpectacleDetailsDTO fetchedDetails = response.body();
                        while (spectacleDetailsList.size() <= position) {
                            spectacleDetailsList.add(null);
                        }
                        spectacleDetailsList.set(position, fetchedDetails);
                        bindPlacesDisponibles(holder, fetchedDetails);
                    } else {
                        holder.placeDisponible.setText("Places disponibles : N/A");
                        holder.placeDisponible.setTextColor(Color.parseColor("#F44336"));
                        holder.placeDisponible.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Teinte rouge pour l'icône en cas d'erreur
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Log.e("SpectacleAdapter", "Failed to fetch details for spectacle ID: " + spectacle.getId() + ", HTTP Code: " + response.code() + ", Error: " + errorBody);
                        } catch (Exception e) {
                            Log.e("SpectacleAdapter", "Error reading error body for spectacle ID: " + spectacle.getId(), e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<SpectacleDetailsDTO> call, Throwable t) {
                    holder.placeDisponible.setText("Places disponibles : N/A");
                    holder.placeDisponible.setTextColor(Color.parseColor("#F44336"));
                    holder.placeDisponible.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Teinte rouge pour l'icône en cas d'échec réseau
                    Log.e("SpectacleAdapter", "Network failure while fetching details for spectacle ID: " + spectacle.getId() + ", Error: " + t.getMessage(), t);
                }
            });
        }

        Glide.with(holder.itemView.getContext())
                .load(spectacle.getImageURL())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            SpectacleDetailsDTO updatedDetails = (spectacleDetailsList != null && position < spectacleDetailsList.size())
                    ? spectacleDetailsList.get(position)
                    : null;
            listener.onSpectacleClick(spectacle, updatedDetails);
        });
    }

    private void bindPlacesDisponibles(ViewHolder holder, SpectacleDetailsDTO spectacleDetails) {
        Integer placesDisponibles = spectacleDetails.getPlacesDisponibles();
        holder.placeDisponible.setText("Places disponibles : " + (placesDisponibles != null ? placesDisponibles : "N/A"));
        if (placesDisponibles != null && placesDisponibles > 0) {
            holder.placeDisponible.setTextColor(Color.parseColor("#4CAF50")); // Vert pour le texte
            holder.placeDisponible.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50"))); // Teinte verte pour l'icône
        } else {
            holder.placeDisponible.setTextColor(Color.parseColor("#F44336")); // Rouge pour le texte
            holder.placeDisponible.setCompoundDrawableTintList(ColorStateList.valueOf(Color.parseColor("#F44336"))); // Teinte rouge pour l'icône
        }
    }

    @Override
    public int getItemCount() {
        return spectacleList != null ? spectacleList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView lieuTextView;
        TextView placeDisponible;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.spectacle_image);
            titleTextView = itemView.findViewById(R.id.spectacle_title);
            lieuTextView = itemView.findViewById(R.id.spectacle_lieu);
            placeDisponible = itemView.findViewById(R.id.place_disponible);
        }
    }

    public void updateLists(List<SpectacleDTO> newSpectacleList, List<SpectacleDetailsDTO> newSpectacleDetailsList) {
        this.spectacleList = new ArrayList<>(newSpectacleList);
        this.spectacleDetailsList = newSpectacleDetailsList != null ? new ArrayList<>(newSpectacleDetailsList) : new ArrayList<>();
        notifyDataSetChanged();
    }
}