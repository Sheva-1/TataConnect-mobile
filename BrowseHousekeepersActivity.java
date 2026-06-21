package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BrowseHousekeepersActivity extends AppCompatActivity {

    private RecyclerView rvHousekeepers;
    private HousekeeperAdapter adapter;
    private List<User> housekeeperList;
    private List<User> fullHousekeeperList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_browse_housekeepers);
        
        db = FirebaseFirestore.getInstance();
        housekeeperList = new ArrayList<>();
        fullHousekeeperList = new ArrayList<>();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        progressBar = findViewById(R.id.progressBar);
        rvHousekeepers = findViewById(R.id.rvHousekeepers);
        rvHousekeepers.setLayoutManager(new LinearLayoutManager(this));
        
        adapter = new HousekeeperAdapter(housekeeperList, user -> {
            Intent intent = new Intent(BrowseHousekeepersActivity.this, HousekeeperProfileActivity.class);
            intent.putExtra("UID", user.getUid()); // UID is now correctly set
            intent.putExtra("NAME", user.getFullName());
            intent.putExtra("LOCATION", user.getLocation());
            intent.putExtra("EXPERIENCE", user.getExperience());
            startActivity(intent);
        });
        rvHousekeepers.setAdapter(adapter);

        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.getText().toString());
                return true;
            }
            return false;
        });

        loadHousekeepers();
    }

    private void loadHousekeepers() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("users")
                .whereEqualTo("userType", "EMPLOYEE")
                .whereEqualTo("isProfileComplete", true)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        fullHousekeeperList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            // CRITICAL FIX: Set the UID from the document ID
                            user.setUid(document.getId()); 
                            fullHousekeeperList.add(user);
                        }
                        housekeeperList.clear();
                        housekeeperList.addAll(fullHousekeeperList);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            housekeeperList.clear();
            housekeeperList.addAll(fullHousekeeperList);
            adapter.notifyDataSetChanged();
            return;
        }

        List<User> filteredList = new ArrayList<>();
        for (User user : fullHousekeeperList) {
            boolean matchesName = user.getFullName() != null && user.getFullName().toLowerCase().contains(query.toLowerCase());
            boolean matchesLocation = user.getLocation() != null && user.getLocation().toLowerCase().contains(query.toLowerCase());
            boolean matchesSkills = user.getSkills() != null && user.getSkills().toLowerCase().contains(query.toLowerCase());
            
            if (matchesName || matchesLocation || matchesSkills) {
                filteredList.add(user);
            }
        }
        
        housekeeperList.clear();
        housekeeperList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}
