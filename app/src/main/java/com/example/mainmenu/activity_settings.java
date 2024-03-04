package com.example.mainmenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class activity_settings extends AppCompatActivity {

    private boolean music;
    private boolean sfx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize switches
        Switch switchSound = findViewById(R.id.switch_sound);
        Switch switchMusic = findViewById(R.id.switch_music);

        // Set default values for switches
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        switchSound.setChecked(!prefs.getBoolean("isMutedSfx", false));
        switchMusic.setChecked(!prefs.getBoolean("isMuted", false));

        // Add listeners to switches
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle sound settings change
                SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                editor.putBoolean("isMutedSfx", !isChecked);
                editor.apply();
            }
        });

        switchMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Handle background music settings change
                SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();
                editor.putBoolean("isMuted", !isChecked);
                editor.apply();
            }
        });

        // Button to go back to the menu
        Button btnBackToMenu = findViewById(R.id.btn_settings);
        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_settings.this, MainActivity.class));
                finish(); // Close this activity
            }
        });
    }

}