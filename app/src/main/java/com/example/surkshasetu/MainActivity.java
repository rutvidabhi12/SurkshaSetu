package com.example.surkshasetu;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // =====================================================
        // STATUS BAR
        // =====================================================

        getWindow().setStatusBarColor(Color.rgb(5, 27, 75));

        // White status bar icons
        getWindow().getDecorView().setSystemUiVisibility(0);

        // Bottom navigation/system bar
        getWindow().setNavigationBarColor(Color.WHITE);

        if (android.os.Build.VERSION.SDK_INT >= 28) {
            getWindow().setNavigationBarDividerColor(Color.WHITE);
        }

        // =====================================================
        // HOME
        // =====================================================

        setContentView(R.layout.activity_home);

        setupCards();
        setupClicks();
        setupBottomNavigation();
    }


    // =====================================================
    // SERVICE CARDS
    // =====================================================

    private void setupCards() {

        setupCard(
                R.id.profilecardd,
                R.drawable.profilecardd,
                "My Profile",
                "View and manage your profile"
        );

        setupCard(
                R.id.safetycardd,
                R.drawable.safetycardd,
                "Safety Tips",
                "Important tips for your safety"
        );

        setupCard(
                R.id.feedbackcardd,
                R.drawable.feedbackcardd,
                "Feedback",
                "Share your feedback or suggestions"
        );

        setupCard(
                R.id.mapcardd,
                R.drawable.mapcardd,
                "Campus Map",
                "View important campus locations"
        );

        setupCard(
                R.id.lostfoundcardd,
                R.drawable.lostfoundcardd,
                "Lost & Found",
                "Report or search for lost items"
        );

        setupCard(
                R.id.complaincardd,
                R.drawable.complaincardd,
                "Complaint",
                "Lodge a complaint or issue"
        );
    }


    // =====================================================
    // COMMON CARD METHOD
    // =====================================================

    private void setupCard(
            int cardId,
            int imageId,
            String title,
            String description) {

        View card = findViewById(cardId);

        ImageView icon = card.findViewById(R.id.imgService);
        TextView titleText = card.findViewById(R.id.txtService);

        icon.setImageResource(imageId);
        titleText.setText(title);
    }


    // =====================================================
    // CLICK EVENTS
    // =====================================================

    private void setupClicks() {

        // =====================================================
        // SOS
        // =====================================================

        findViewById(R.id.cardSOS).setOnClickListener(v -> {
            makeCall("112");
        });


        // =====================================================
        // MY PROFILE
        // =====================================================

        findViewById(R.id.profilecardd).setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "My Profile",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // =====================================================
        // SAFETY TIPS
        // =====================================================

        findViewById(R.id.safetycardd).setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Safety Tips",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // =====================================================
        // FEEDBACK
        // =====================================================

        findViewById(R.id.feedbackcardd).setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Feedback",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // =====================================================
        // CAMPUS MAP
        // =====================================================

        findViewById(R.id.mapcardd).setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Campus Map",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // =====================================================
        // LOST & FOUND
        // =====================================================

        findViewById(R.id.lostfoundcardd).setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Lost & Found",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // =====================================================
        // COMPLAINT
        // =====================================================

        findViewById(R.id.complaincardd).setOnClickListener(v -> {
            Toast.makeText(
                    this,
                    "Complaint",
                    Toast.LENGTH_SHORT
            ).show();
        });


        // =====================================================
        // IMPORTANT ANNOUNCEMENT
        // =====================================================

        findViewById(R.id.btnViewAnnouncement).setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Announcement",
                    Toast.LENGTH_SHORT
            ).show();

        });
    }


    // =====================================================
    // BOTTOM NAVIGATION
    // =====================================================

    private void setupBottomNavigation() {

        // =====================================================
        // HOME
        // =====================================================

        findViewById(R.id.navHome).setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Home",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // =====================================================
        // CONTACTS
        // =====================================================

        findViewById(R.id.navContacts).setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Contacts",
                    Toast.LENGTH_SHORT
            ).show();

        });


        // PROFILE
        findViewById(R.id.navProfile).setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "Profile",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }


    // =====================================================
    // SOS CALL
    // =====================================================

    private void makeCall(String number) {

        Intent intent = new Intent(
                Intent.ACTION_DIAL,
                Uri.parse("tel:" + number)
        );

        startActivity(intent);
    }
}