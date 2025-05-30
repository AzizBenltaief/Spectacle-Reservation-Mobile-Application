package com.example.spectacleprojet.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spectacleprojet.DTO.ProgrammationDTO;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
    private List<ProgrammationDTO> dates;
    private Context context;

    public DateAdapter(Context context, List<ProgrammationDTO> dates) {
        this.context = context;
        this.dates = dates;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        ProgrammationDTO programmation = dates.get(position);
        String dateText = programmation.getDate() + " à " + programmation.getHeure();
        holder.dateTextView.setText(dateText);
    }

    @Override
    public int getItemCount() {
        return dates != null ? dates.size() : 0;
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}