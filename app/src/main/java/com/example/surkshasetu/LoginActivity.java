package com.example.surkshasetu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =====================================================
        // LOGIN LAYOUT
        // =====================================================

        setContentView(R.layout.activity_login);

        // =====================================================
        // STATUS BAR
        // =====================================================

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);

        // =====================================================
        // DISABLE AUTOFILL
        // =====================================================

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            getWindow().getDecorView().setImportantForAutofill(
                    View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
            );
        }

        // =====================================================
        // FIND VIEWS
        // =====================================================

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        btnLogin.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // =================================================
            // USERNAME VALIDATION
            // =================================================

            if (username.isEmpty()) {

                etUsername.setError("Enter User ID / Email");
                etUsername.requestFocus();

                return;
            }

            // =================================================
            // PASSWORD VALIDATION
            // =================================================

            if (password.isEmpty()) {

                etPassword.setError("Enter Password");
                etPassword.requestFocus();

                return;
            }

            // =================================================
            // LOGIN SUCCESS
            // =================================================

            Toast.makeText(
                    LoginActivity.this,
                    "✓ Login successful",
                    Toast.LENGTH_SHORT
            ).show();

            // =================================================
            // OPEN HOME PAGE
            // =================================================

            Intent intent = new Intent(
                    LoginActivity.this,
                    MainActivity.class
            );

            startActivity(intent);

            // Login page close
            finish();
        });
    }
}