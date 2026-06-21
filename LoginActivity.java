package com.example.tataconnect;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private AppCompatButton btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        LinearLayout btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        btnLogin.setOnClickListener(v -> loginUser());

        TextView tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, AccountTypeActivity.class);
            startActivity(intent);
        });

        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
    }

    private void showForgotPasswordDialog() {
        FrameLayout container = new FrameLayout(this);
        EditText resetMail = new EditText(this);
        resetMail.setHint("Enter your email");
        
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT);
        
        int margin = (int) (20 * getResources().getDisplayMetrics().density);
        params.leftMargin = margin;
        params.rightMargin = margin;
        params.topMargin = margin / 2;
        resetMail.setLayoutParams(params);
        container.addView(resetMail);

        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(this);
        passwordResetDialog.setTitle("Reset Password?");
        passwordResetDialog.setMessage("Enter your email to receive a password reset link.");
        passwordResetDialog.setView(container);

        passwordResetDialog.setPositiveButton("Send Link", (dialog, which) -> {
            String mail = resetMail.getText().toString().trim();
            if (TextUtils.isEmpty(mail)) {
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> 
                Toast.makeText(LoginActivity.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show()
            ).addOnFailureListener(e -> 
                Toast.makeText(LoginActivity.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        });

        passwordResetDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        passwordResetDialog.create().show();
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }

        setLoading(true);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        // TEMPORARY BYPASS FOR TESTING: 
                        // We will allow login even if not verified, but show a Toast warning.
                        if (user != null) {
                            if (!user.isEmailVerified()) {
                                Toast.makeText(this, "Demo Mode: Logging in without verification", Toast.LENGTH_LONG).show();
                            }
                            checkUserTypeAndNavigate();
                        }
                    } else {
                        setLoading(false);
                        Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            btnLogin.setText("");
            btnLogin.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setText(R.string.sign_in);
            btnLogin.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void checkUserTypeAndNavigate() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid).get().addOnCompleteListener(task -> {
            setLoading(false);
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String userType = document.getString("userType");
                    Boolean isProfileComplete = document.getBoolean("isProfileComplete");
                    if (isProfileComplete == null) isProfileComplete = false;

                    Intent intent;
                    if (!isProfileComplete) {
                        if ("FAMILY".equals(userType)) {
                            intent = new Intent(LoginActivity.this, CompleteProfileFamilyActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, CompleteProfileEmployeeActivity.class);
                        }
                    } else {
                        if ("FAMILY".equals(userType)) {
                            intent = new Intent(LoginActivity.this, HomeFamilyActivity.class);
                        } else {
                            intent = new Intent(LoginActivity.this, HomeEmployeeActivity.class);
                        }
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    startActivity(new Intent(LoginActivity.this, AccountTypeActivity.class));
                    finish();
                }
            } else {
                Toast.makeText(this, "Error fetching profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
