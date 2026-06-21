package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeEmployeeActivity extends AppCompatActivity {

    private TextView tvEmployeeName, tvStatusValue;
    private RecyclerView rvFamilies;
    private FamilyAdapter adapter;
    private List<User> familyList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isAvailable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_employee);
        
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        familyList = new ArrayList<>();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0); 
            return insets;
        });

        tvEmployeeName = findViewById(R.id.tvEmployeeName);
        tvStatusValue = findViewById(R.id.tvStatusValue);
        rvFamilies = findViewById(R.id.rvFamilies);
        
        rvFamilies.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FamilyAdapter(familyList, user -> {
            Intent intent = new Intent(HomeEmployeeActivity.this, FamilyProfileActivity.class);
            intent.putExtra("FAMILY_NAME", user.getFullName());
            intent.putExtra("LOCATION", user.getLocation());
            intent.putExtra("BUDGET", String.format("%,d FCFA/hr", user.getMaxBudget()));
            // CRITICAL: Ensure UID is passed
            intent.putExtra("OTHER_USER_ID", user.getUid());
            startActivity(intent);
        });
        rvFamilies.setAdapter(adapter);

        setupNavigation();
        loadUserProfile();
        loadAvailableFamilies();
    }

    private void loadUserProfile() {
        if (mAuth.getCurrentUser() == null) return;

        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    String fullName = doc.getString("fullName");
                    if (fullName != null) {
                        tvEmployeeName.setText(fullName);
                    }
                    
                    Boolean status = doc.getBoolean("isAvailable");
                    if (status != null) {
                        isAvailable = status;
                        updateStatusUI();
                    }
                }
            }
        });
    }

    private void loadAvailableFamilies() {
        db.collection("users")
                .whereEqualTo("userType", "FAMILY")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        familyList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User family = document.toObject(User.class);
                            // CRITICAL FIX: Set the UID from the Firestore document ID
                            family.setUid(document.getId());
                            familyList.add(family);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void updateStatusUI() {
        if (isAvailable) {
            tvStatusValue.setText("Available");
            tvStatusValue.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        } else {
            tvStatusValue.setText("Busy");
            tvStatusValue.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        }
    }

    private void toggleStatus() {
        isAvailable = !isAvailable;
        updateStatusUI();
        
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid)
                .update("isAvailable", isAvailable)
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show());
    }

    private void setupNavigation() {
        findViewById(R.id.statusLayout).setOnClickListener(v -> toggleStatus());

        View.OnClickListener goToInbox = v -> {
            Intent intent = new Intent(HomeEmployeeActivity.this, InboxActivity.class);
            startActivity(intent);
        };
        findViewById(R.id.cardMessages).setOnClickListener(goToInbox);
        findViewById(R.id.navMessages).setOnClickListener(goToInbox);

        findViewById(R.id.cardLogout).setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(HomeEmployeeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
