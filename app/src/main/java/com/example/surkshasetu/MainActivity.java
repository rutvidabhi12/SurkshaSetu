package com.example.surkshasetu;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);

       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            getWindow().getDecorView().setImportantForAutofill(
                    View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
            );
        }

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

            Toast toast = new Toast(MainActivity.this);
            TextView toastText = new TextView(MainActivity.this);

            toastText.setText("✓  Login successfull");
            toastText.setTextColor(Color.WHITE);
            toastText.setTextSize(16);
            toastText.setGravity(Gravity.CENTER);
            toastText.setTypeface(null, Typeface.BOLD);

            toastText.setPadding(35, 18, 35, 18);

            toastText.setBackgroundResource(
                    R.drawable.bg_success_toast
            );

            toast.setView(toastText);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();

            etUsername.setText("");
            etPassword.setText("");
        });
    }
}