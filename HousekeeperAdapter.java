package com.example.tataconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class HousekeeperAdapter extends RecyclerView.Adapter<HousekeeperAdapter.ViewHolder> {

    private List<User> housekeeperList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public HousekeeperAdapter(List<User> housekeeperList, OnItemClickListener listener) {
        this.housekeeperList = housekeeperList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_housekeeper, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = housekeeperList.get(position);
        holder.tvName.setText(user.getFullName());
        holder.tvExperience.setText(user.getExperience() + " years experience");
        holder.tvLocation.setText(user.getLocation());
        holder.tvSkills.setText(user.getSkills());
        holder.tvSalary.setText(String.format(Locale.getDefault(), "%,d FCFA/hr", user.getExpectedSalary()));
        
        if (user.getFullName() != null && !user.getFullName().isEmpty()) {
            holder.tvInitial.setText(user.getFullName().substring(0, 1).toUpperCase());
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(user));
    }

    @Override
    public int getItemCount() {
        return housekeeperList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvExperience, tvLocation, tvSkills, tvSalary, tvInitial;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSkills = itemView.findViewById(R.id.tvSkills);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvInitial = itemView.findViewById(R.id.tvInitial);
        }
    }
}