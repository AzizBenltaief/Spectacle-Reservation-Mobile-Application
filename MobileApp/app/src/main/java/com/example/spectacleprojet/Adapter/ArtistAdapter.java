package com.example.spectacleprojet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spectacleprojet.DTO.ArtisteDTO;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private List<ArtisteDTO> artists;
    private Context context;

    public ArtistAdapter(Context context, List<ArtisteDTO> artists) {
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        ArtisteDTO artiste = artists.get(position);
        String artistText = artiste.getNom() + " " + artiste.getPrenom() + " (" + artiste.getSpecialite() + ")";
        holder.artistTextView.setText(artistText);
    }

    @Override
    public int getItemCount() {
        return artists != null ? artists.size() : 0;
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistTextView;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}