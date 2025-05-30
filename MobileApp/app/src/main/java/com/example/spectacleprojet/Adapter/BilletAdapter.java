package com.example.spectacleprojet.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spectacleprojet.DTO.BilletResponseDTO;
import com.example.spectacleprojet.R;

import java.util.ArrayList;
import java.util.List;

public class BilletAdapter extends RecyclerView.Adapter<BilletAdapter.BilletViewHolder> {
    private static final String TAG = "BilletAdapter";
    private List<BilletResponseDTO> billetDetailsList;
    private OnCancelClickListener cancelClickListener;

    // Callback interface for cancel button clicks
    public interface OnCancelClickListener {
        void onCancelClick(int billetId, int position);
    }

    public BilletAdapter(List<BilletResponseDTO> billetDetailsList, OnCancelClickListener listener) {
        this.billetDetailsList = billetDetailsList != null ? billetDetailsList : new ArrayList<>();
        this.cancelClickListener = listener;
    }

    @NonNull
    @Override
    public BilletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new BilletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BilletViewHolder holder, int position) {
        BilletResponseDTO billet = billetDetailsList.get(position);

        // Bind Spectacle Title
        if (holder.spectacleTitleTextView != null) {
            holder.spectacleTitleTextView.setText(billet.getSpectacleTitre() != null ? billet.getSpectacleTitre() : "Titre indisponible");
        } else {
            Log.e(TAG, "spectacleTitleTextView is null at position: " + position);
        }

        // Bind Date
        if (holder.dateTextView != null) {
            holder.dateTextView.setText(billet.getDate() != null ? billet.getDate() : "N/A");
        } else {
            Log.e(TAG, "dateTextView is null at position: " + position);
        }

        // Bind Heure (Time)
        if (holder.timeTextView != null) {
            holder.timeTextView.setText(billet.getHeure() != null ? billet.getHeure() : "N/A");
        } else {
            Log.e(TAG, "timeTextView is null at position: " + position);
        }

        // Bind Prix
        if (holder.prixTextView != null) {
            holder.prixTextView.setText(billet.getPrix() != null ? billet.getPrix() + " TND" : "N/A");
        } else {
            Log.e(TAG, "prixTextView is null at position: " + position);
        }

        // Bind Status
        if (holder.statusTextView != null) {
            holder.statusTextView.setText("CONFIRMÉ");
        } else {
            Log.e(TAG, "statusTextView is null at position: " + position);
        }

        // Bind Annuler TextView (make it clickable)
        if (holder.annulerTextView != null) {
            holder.annulerTextView.setOnClickListener(v -> {
                if (cancelClickListener != null) {
                    cancelClickListener.onCancelClick(billet.getId(), position);
                }
            });
        } else {
            Log.e(TAG, "annulerTextView is null at position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return billetDetailsList.size();
    }

    public void updateList(List<BilletResponseDTO> newList) {
        this.billetDetailsList.clear();
        this.billetDetailsList.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < billetDetailsList.size()) { // Ajout d'une validation
            billetDetailsList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, billetDetailsList.size());
        } else {
            Log.e(TAG, "Tentative de suppression à un index invalide : " + position + ", taille de la liste : " + billetDetailsList.size());
        }
    }

    static class BilletViewHolder extends RecyclerView.ViewHolder {
        TextView spectacleTitleTextView, dateTextView, timeTextView, prixTextView, statusTextView;
        ImageView categoryImageView;
        TextView annulerTextView;

        public BilletViewHolder(@NonNull View itemView) {
            super(itemView);
            spectacleTitleTextView = itemView.findViewById(R.id.tvSpectacleTitle);
            dateTextView = itemView.findViewById(R.id.tvDate);
            timeTextView = itemView.findViewById(R.id.tvTime);
            prixTextView = itemView.findViewById(R.id.tvPrix);
            statusTextView = itemView.findViewById(R.id.tvStatus);
            categoryImageView = itemView.findViewById(R.id.ivCategory);
            annulerTextView = itemView.findViewById(R.id.btnAnnuler);

            // Log if views are null
            if (spectacleTitleTextView == null) {
                Log.e(TAG, "spectacleTitleTextView (tvSpectacleTitle) not found in layout");
            }
            if (dateTextView == null) {
                Log.e(TAG, "dateTextView (tvDate) not found in layout");
            }
            if (timeTextView == null) {
                Log.e(TAG, "timeTextView (tvTime) not found in layout");
            }
            if (prixTextView == null) {
                Log.e(TAG, "prixTextView (tvPrix) not found in layout");
            }
            if (statusTextView == null) {
                Log.e(TAG, "statusTextView (tvStatus) not found in layout");
            }
            if (categoryImageView == null) {
                Log.e(TAG, "categoryImageView (ivCategory) not found in layout");
            }
            if (annulerTextView == null) {
                Log.e(TAG, "annulerTextView (btnAnnuler) not found in layout");
            }
        }
    }
}