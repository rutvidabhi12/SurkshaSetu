package com.example.surkshasetu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);

        MaterialButton btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {

            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

           if (username.isEmpty()) {
                etUsername.setError("Enter User ID / Email");
                etUsername.requestFocus();
                return;
            }


            if (password.isEmpty()) {
                etPassword.setError("Enter Password");
                etPassword.requestFocus();
                return;
            }


            android.widget.Toast.makeText(
                    MainActivity.this,
                    "Login successfull",
                    android.widget.Toast.LENGTH_SHORT
            ).show();
        });
    }
}