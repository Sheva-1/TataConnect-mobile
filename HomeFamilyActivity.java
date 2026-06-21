package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class HomeFamilyActivity extends AppCompatActivity {

    private TextView tvFamilyName, tvLocationValue, tvBudgetValue;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_family);
        
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0); 
            return insets;
        });

        // Initialize Views
        tvFamilyName = findViewById(R.id.tvFamilyName);
        tvLocationValue = findViewById(R.id.tvLocationValue);
        tvBudgetValue = findViewById(R.id.tvBudgetValue);

        setupNavigation();
        loadUserProfile();
    }

    private void loadUserProfile() {
        if (mAuth.getCurrentUser() == null) return;

        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    String fullName = doc.getString("fullName");
                    String location = doc.getString("location");
                    Long minBudget = doc.getLong("minBudget");
                    Long maxBudget = doc.getLong("maxBudget");

                    if (fullName != null) {
                        tvFamilyName.setText(fullName + " Family");
                    }
                    
                    if (location != null) {
                        tvLocationValue.setText(location);
                    }

                    if (minBudget != null && maxBudget != null) {
                        tvBudgetValue.setText(String.format(Locale.getDefault(), "%,d - %,d FCFA/hr", minBudget, maxBudget));
                    }
                }
            }
        });
    }

    private void setupNavigation() {
        View.OnClickListener goToBrowse = v -> {
            Intent intent = new Intent(HomeFamilyActivity.this, BrowseHousekeepersActivity.class);
            startActivity(intent);
        };

        findViewById(R.id.catAll).setOnClickListener(goToBrowse);
        findViewById(R.id.catHousekeeper).setOnClickListener(goToBrowse);
        findViewById(R.id.catNanny).setOnClickListener(goToBrowse);
        findViewById(R.id.catCook).setOnClickListener(goToBrowse);
        findViewById(R.id.cardQuickBrowse).setOnClickListener(goToBrowse);
        findViewById(R.id.navBrowse).setOnClickListener(goToBrowse);
        findViewById(R.id.cardLocation).setOnClickListener(goToBrowse);
        findViewById(R.id.cardBudget).setOnClickListener(goToBrowse);

        View.OnClickListener goToInbox = v -> {
            Intent intent = new Intent(HomeFamilyActivity.this, InboxActivity.class);
            startActivity(intent);
        };

        findViewById(R.id.cardQuickMessages).setOnClickListener(goToInbox);
        findViewById(R.id.navMessages).setOnClickListener(goToInbox);

        // Logout
        findViewById(R.id.cardLogout).setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(HomeFamilyActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}