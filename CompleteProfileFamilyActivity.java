package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CompleteProfileFamilyActivity extends AppCompatActivity {

    private EditText etFamilyName, etLocation, etPhone, etAbout;
    private ChipGroup cgServices;
    private TextView tvFamilySize, tvBudgetRange, tvMinBudget, tvMaxBudget;
    private SeekBar sbFamilySize, sbMinBudget, sbMaxBudget;
    private AppCompatButton btnCompleteSetup;
    private ProgressBar progressBar;

    private int familySize = 1;
    private int minBudget = 1000;
    private int maxBudget = 5000;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_complete_profile_family);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI
        etFamilyName = findViewById(R.id.etFamilyName);
        etLocation = findViewById(R.id.etLocation);
        etPhone = findViewById(R.id.etPhone);
        etAbout = findViewById(R.id.etAbout);
        cgServices = findViewById(R.id.cgServices);
        tvFamilySize = findViewById(R.id.tvFamilySize);
        tvBudgetRange = findViewById(R.id.tvBudgetRange);
        tvMinBudget = findViewById(R.id.tvMinBudget);
        tvMaxBudget = findViewById(R.id.tvMaxBudget);
        sbFamilySize = findViewById(R.id.sbFamilySize);
        sbMinBudget = findViewById(R.id.sbMinBudget);
        sbMaxBudget = findViewById(R.id.sbMaxBudget);
        btnCompleteSetup = findViewById(R.id.btnCompleteSetup);
        progressBar = findViewById(R.id.progressBar);

        LinearLayout btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // UI logic
        sbFamilySize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                familySize = Math.max(1, progress);
                tvFamilySize.setText(String.format(Locale.getDefault(), "Family Size: %d members", familySize));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        BudgetChangeListener budgetListener = new BudgetChangeListener();
        sbMinBudget.setOnSeekBarChangeListener(budgetListener);
        sbMaxBudget.setOnSeekBarChangeListener(budgetListener);

        btnCompleteSetup.setOnClickListener(v -> {
            if (validateFields()) {
                saveToFirestore();
            }
        });
    }

    private void saveToFirestore() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Session expired", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);
        String uid = user.getUid();

        List<String> selectedServices = new ArrayList<>();
        for (int i = 0; i < cgServices.getChildCount(); i++) {
            Chip chip = (Chip) cgServices.getChildAt(i);
            if (chip.isChecked()) {
                selectedServices.add(chip.getText().toString());
            }
        }

        Map<String, Object> profileData = new HashMap<>();
        profileData.put("fullName", etFamilyName.getText().toString().trim());
        profileData.put("location", etLocation.getText().toString().trim());
        profileData.put("phone", etPhone.getText().toString().trim());
        profileData.put("about", etAbout.getText().toString().trim());
        profileData.put("familySize", familySize);
        profileData.put("minBudget", minBudget);
        profileData.put("maxBudget", maxBudget);
        profileData.put("servicesNeeded", TextUtils.join(", ", selectedServices));
        profileData.put("isProfileComplete", true);

        db.collection("users").document(uid)
                .set(profileData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    setLoading(false);
                    Intent intent = new Intent(CompleteProfileFamilyActivity.this, HomeFamilyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            btnCompleteSetup.setText("");
            btnCompleteSetup.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnCompleteSetup.setText(R.string.complete_setup);
            btnCompleteSetup.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
    }

    private class BudgetChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            minBudget = sbMinBudget.getProgress();
            maxBudget = sbMaxBudget.getProgress();
            updateBudgetLabels();
        }
        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    }

    private boolean validateFields() {
        if (etFamilyName.getText().toString().trim().isEmpty()) {
            etFamilyName.setError("Family name required");
            return false;
        }
        if (etLocation.getText().toString().trim().isEmpty()) {
            etLocation.setError("Location required");
            return false;
        }
        if (cgServices.getCheckedChipIds().isEmpty()) {
            Toast.makeText(this, "Select at least one service", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void updateBudgetLabels() {
        tvMinBudget.setText(String.format(Locale.getDefault(), "Minimum: %,d FCFA", minBudget));
        tvMaxBudget.setText(String.format(Locale.getDefault(), "Maximum: %,d FCFA", maxBudget));
        tvBudgetRange.setText(String.format(Locale.getDefault(), "Budget Range: %,d - %,d FCFA/hour", minBudget, maxBudget));
    }
}
