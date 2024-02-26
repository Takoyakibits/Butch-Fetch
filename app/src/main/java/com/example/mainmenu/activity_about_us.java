package com.example.mainmenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class activity_about_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Button btnBackToMenu = findViewById(R.id.btn_Back_to_Menu);

        btnBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the MainActivity immediately after creating the intent
                Intent intent = new Intent(activity_about_us.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close this activity
            }
        });
    }
}
