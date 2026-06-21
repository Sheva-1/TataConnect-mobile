package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class HousekeeperProfileActivity extends AppCompatActivity {

    private String housekeeperId;
    private String housekeeperName;
    private FirebaseFirestore db;

    private TextView tvName, tvRole, tvLocation, tvExperience, tvProfileInitial, tvAbout, tvRating, tvReviews, tvPrice;
    private LinearLayout layoutSkills, layoutLanguages;
    private GridLayout layoutAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_housekeeper_profile);

        db = FirebaseFirestore.getInstance();

        // Get initial data from intent
        housekeeperId = getIntent().getStringExtra("UID");
        housekeeperName = getIntent().getStringExtra("NAME");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        tvName = findViewById(R.id.tvName);
        tvRole = findViewById(R.id.tvRole);
        tvLocation = findViewById(R.id.tvLocation);
        tvExperience = findViewById(R.id.tvExperience);
        tvProfileInitial = findViewById(R.id.tvProfileInitial);
        tvAbout = findViewById(R.id.tvAbout);
        tvRating = findViewById(R.id.tvRating);
        tvReviews = findViewById(R.id.tvReviews);
        tvPrice = findViewById(R.id.tvPrice);
        
        layoutSkills = findViewById(R.id.layoutSkills);
        layoutLanguages = findViewById(R.id.layoutLanguages);
        layoutAvailability = findViewById(R.id.layoutAvailability);

        ImageView btnBack = findViewById(R.id.btnBack);
        View btnSendMessage = findViewById(R.id.btnSendMessage);

        // Set Initial Data
        if (housekeeperName != null) {
            tvName.setText(housekeeperName);
            tvProfileInitial.setText(housekeeperName.substring(0, 1).toUpperCase());
        }

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Send Message Button
        btnSendMessage.setOnClickListener(v -> {
            if (housekeeperId != null) {
                Intent intent = new Intent(HousekeeperProfileActivity.this, ChatActivity.class);
                intent.putExtra("OTHER_USER_ID", housekeeperId);
                intent.putExtra("OTHER_USER_NAME", housekeeperName);
                startActivity(intent);
            }
        });

        fetchFullProfile();
    }

    private void fetchFullProfile() {
        if (housekeeperId == null) return;

        db.collection("users").document(housekeeperId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    updateUI(user);
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUI(User user) {
        tvName.setText(user.getFullName());
        tvProfileInitial.setText(user.getFullName() != null ? user.getFullName().substring(0, 1).toUpperCase() : "U");
        tvLocation.setText(user.getLocation());
        tvExperience.setText(user.getExperience() + " years");
        tvRole.setText(user.getSkills()); // Often used as a primary role
        tvAbout.setText(user.getAbout() != null ? user.getAbout() : "No description provided.");
        tvPrice.setText(String.format(Locale.getDefault(), "%,d FCFA", user.getExpectedSalary()));
        
        // Dynamic Skills
        if (user.getSkills() != null) {
            layoutSkills.removeAllViews();
            String[] skills = user.getSkills().split(",");
            for (String skill : skills) {
                TextView tvSkill = new TextView(this);
                tvSkill.setText("• " + skill.trim());
                tvSkill.setTextColor(getResources().getColor(R.color.text_secondary));
                layoutSkills.addView(tvSkill);
            }
        }

        // Dynamic Languages
        if (user.getLanguages() != null) {
            layoutLanguages.removeAllViews();
            String[] langs = user.getLanguages().split(",");
            for (String lang : langs) {
                TextView tvLang = new TextView(this);
                tvLang.setText(lang.trim());
                tvLang.setBackgroundResource(R.drawable.bg_tag_pill);
                tvLang.setPadding(24, 8, 24, 8);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 16, 0);
                tvLang.setLayoutParams(params);
                layoutLanguages.addView(tvLang);
            }
        }
    }
}
