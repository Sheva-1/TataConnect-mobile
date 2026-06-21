package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class FamilyProfileActivity extends AppCompatActivity {

    private String familyId;
    private String familyName;
    private FirebaseFirestore db;

    private TextView tvName, tvLoc, tvBud, tvFamilySize;
    private TextView tvServicesNeeded, tvRequirements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_family_profile);

        db = FirebaseFirestore.getInstance();

        // Get initial data from Intent
        familyId = getIntent().getStringExtra("OTHER_USER_ID");
        familyName = getIntent().getStringExtra("FAMILY_NAME");
        String location = getIntent().getStringExtra("LOCATION");
        String budget = getIntent().getStringExtra("BUDGET");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        tvName = findViewById(R.id.tvFamilyName);
        tvLoc = findViewById(R.id.tvLocation);
        tvBud = findViewById(R.id.tvBudget);
        tvFamilySize = findViewById(R.id.tvFamilySize);
        // Assuming these IDs exist in XML based on layout review
        // tvServicesNeeded = findViewById(R.id.tvServicesNeeded); // In the layout it's nested in cards
        // tvRequirements = findViewById(R.id.tvRequirements);

        ImageView btnBack = findViewById(R.id.btnBack);
        AppCompatButton btnChat = findViewById(R.id.btnChat);
        AppCompatButton btnReject = findViewById(R.id.btnReject);

        // Set Initial Data
        if (familyName != null) tvName.setText(familyName);
        if (location != null) tvLoc.setText(location);
        if (budget != null) tvBud.setText(budget);

        btnBack.setOnClickListener(v -> finish());
        btnReject.setOnClickListener(v -> finish());

        btnChat.setOnClickListener(v -> {
            if (familyId != null) {
                Intent intent = new Intent(FamilyProfileActivity.this, ChatActivity.class);
                intent.putExtra("OTHER_USER_ID", familyId);
                intent.putExtra("OTHER_USER_NAME", familyName);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Family ID missing", Toast.LENGTH_SHORT).show();
            }
        });

        fetchFullProfile();
    }

    private void fetchFullProfile() {
        if (familyId == null) return;

        db.collection("users").document(familyId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    updateUI(user);
                }
            }
        });
    }

    private void updateUI(User user) {
        tvName.setText(user.getFullName() + " Family");
        tvLoc.setText(user.getLocation());
        tvBud.setText(String.format(Locale.getDefault(), "%,d FCFA", user.getMaxBudget()));
        tvFamilySize.setText(user.getFamilySize() + " members");
        
        // Requirements and services are often in description/about for families
        // We could map them if the layout had specific IDs for details
    }
}
