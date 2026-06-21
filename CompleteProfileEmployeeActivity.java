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

public class CompleteProfileEmployeeActivity extends AppCompatActivity {

    private EditText etFullName, etLocation, etAbout;
    private ChipGroup cgSkills, cgLanguages;
    private TextView tvExperience, tvSalary;
    private SeekBar sbExperience, sbSalary;
    private AppCompatButton btnCompleteSetup;
    private ProgressBar progressBar;

    private int experience = 0;
    private int salary = 1000;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_complete_profile_employee);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI elements
        etFullName = findViewById(R.id.etFullName);
        etLocation = findViewById(R.id.etLocation);
        etAbout = findViewById(R.id.etAbout);
        cgSkills = findViewById(R.id.cgSkills);
        cgLanguages = findViewById(R.id.cgLanguages);
        tvExperience = findViewById(R.id.tvExperience);
        tvSalary = findViewById(R.id.tvSalary);
        sbExperience = findViewById(R.id.sbExperience);
        sbSalary = findViewById(R.id.sbSalary);
        btnCompleteSetup = findViewById(R.id.btnCompleteSetup);
        progressBar = findViewById(R.id.progressBar);

        LinearLayout btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // Logic for Experience Slider
        if (sbExperience != null) {
            sbExperience.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    experience = progress;
                    if (tvExperience != null) {
                        tvExperience.setText(String.format(Locale.getDefault(), "Years of Experience: %d years", experience));
                    }
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        // Logic for Salary Slider
        if (sbSalary != null) {
            sbSalary.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    salary = Math.max(500, progress); // Minimum 500
                    if (tvSalary != null) {
                        tvSalary.setText(String.format(Locale.getDefault(), "Expected Salary: %,d FCFA/hour", salary));
                    }
                }
                @Override public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        if (btnCompleteSetup != null) {
            btnCompleteSetup.setOnClickListener(v -> {
                if (validateFields()) {
                    saveToFirestore();
                }
            });
        }
    }

    private void saveToFirestore() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Session expired. Please sign in again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setLoading(true);
        String uid = user.getUid();

        List<String> selectedSkills = new ArrayList<>();
        if (cgSkills != null) {
            for (int i = 0; i < cgSkills.getChildCount(); i++) {
                View view = cgSkills.getChildAt(i);
                if (view instanceof Chip) {
                    Chip chip = (Chip) view;
                    if (chip.isChecked()) {
                        selectedSkills.add(chip.getText().toString());
                    }
                }
            }
        }

        List<String> selectedLanguages = new ArrayList<>();
        if (cgLanguages != null) {
            for (int i = 0; i < cgLanguages.getChildCount(); i++) {
                View view = cgLanguages.getChildAt(i);
                if (view instanceof Chip) {
                    Chip chip = (Chip) view;
                    if (chip.isChecked()) {
                        selectedLanguages.add(chip.getText().toString());
                    }
                }
            }
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("fullName", etFullName.getText().toString().trim());
        updates.put("location", etLocation.getText().toString().trim());
        updates.put("about", etAbout.getText().toString().trim());
        updates.put("experience", experience);
        updates.put("expectedSalary", salary);
        updates.put("skills", TextUtils.join(", ", selectedSkills));
        updates.put("languages", TextUtils.join(", ", selectedLanguages));
        updates.put("isProfileComplete", true);

        db.collection("users").document(uid)
                .set(updates, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    setLoading(false);
                    Intent intent = new Intent(CompleteProfileEmployeeActivity.this, HomeEmployeeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    setLoading(false);
                    Toast.makeText(this, "Firestore error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private boolean validateFields() {
        if (etFullName == null || etLocation == null || cgSkills == null) return false;

        String name = etFullName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return false;
        }

        if (location.isEmpty()) {
            etLocation.setError("Location is required");
            etLocation.requestFocus();
            return false;
        }

        if (cgSkills.getCheckedChipIds().isEmpty()) {
            Toast.makeText(this, "Please select at least one skill", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
