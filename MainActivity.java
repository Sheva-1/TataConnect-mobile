package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Persist login state
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        
        if (user != null) {
            // TEMPORARY BYPASS FOR DEMO: Allowing auto-login even if email is not verified
            checkUserTypeAndNavigate(user.getUid());
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton btnGetStarted = findViewById(R.id.btnGetStarted);
        if (btnGetStarted != null) {
            btnGetStarted.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AccountTypeActivity.class);
                startActivity(intent);
            });
        }

        MaterialButton btnSignIn = findViewById(R.id.btnSignIn);
        if (btnSignIn != null) {
            btnSignIn.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }
    }

    private void checkUserTypeAndNavigate(String uid) {
        FirebaseFirestore.getInstance().collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userType = documentSnapshot.getString("userType");
                        Boolean isProfileComplete = documentSnapshot.getBoolean("isProfileComplete");
                        if (isProfileComplete == null) isProfileComplete = false;

                        Intent intent;
                        if (!isProfileComplete) {
                            if ("FAMILY".equals(userType)) {
                                intent = new Intent(MainActivity.this, CompleteProfileFamilyActivity.class);
                            } else {
                                intent = new Intent(MainActivity.this, CompleteProfileEmployeeActivity.class);
                            }
                        } else {
                            if ("FAMILY".equals(userType)) {
                                intent = new Intent(MainActivity.this, HomeFamilyActivity.class);
                            } else {
                                intent = new Intent(MainActivity.this, HomeEmployeeActivity.class);
                            }
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // If user exists in Auth but not in Firestore
                        startActivity(new Intent(MainActivity.this, AccountTypeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    // Fail gracefully, show welcome screen
                    setContentView(R.layout.activity_main);
                });
    }
}
