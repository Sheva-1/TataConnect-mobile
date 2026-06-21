package com.example.tataconnect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.ViewHolder> {

    private List<User> familyList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public FamilyAdapter(List<User> familyList, OnItemClickListener listener) {
        this.familyList = familyList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = familyList.get(position);
        holder.tvFamilyName.setText(user.getFullName() != null ? user.getFullName() + " Family" : "Unknown Family");
        
        String location = user.getLocation() != null ? user.getLocation() : "Unknown Location";
        String budget = String.format(Locale.getDefault(), "%,d FCFA/hr", user.getMaxBudget());
        holder.tvFamilyDetails.setText(location + " • Budget: " + budget);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(user));
    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFamilyName, tvFamilyDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFamilyName = itemView.findViewById(R.id.tvFamilyName);
            tvFamilyDetails = itemView.findViewById(R.id.tvFamilyDetails);
        }
    }
}
