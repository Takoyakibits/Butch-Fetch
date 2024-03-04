package com.example.mainmenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    VideoView videoView;
    Button btnStartGame, btnTutorial, btnSettings, btnAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoview);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_video);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        // Initialize buttons and set click listeners
        btnStartGame = findViewById(R.id.btn_start_game);
        btnTutorial = findViewById(R.id.btn_tutorial);
        btnSettings = findViewById(R.id.btn_settings);
        btnAboutUs = findViewById(R.id.btn_about_us);

        btnStartGame.setOnClickListener(this);
        btnTutorial.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnAboutUs.setOnClickListener(this);
        SoundPlayer.initialize(this);
    }
    @Override
    protected void onResume() {
        videoView.resume();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
    }

    @Override
    protected void onPause() {
        videoView.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        super.onDestroy();
    }



    @Override
    public void onClick(View view) {
        Intent intent;
        int viewId = view.getId();
        if (viewId == R.id.btn_start_game) {
            playSFXButton();
            intent = new Intent(this, activity_start_game.class);
        } else if (viewId == R.id.btn_tutorial) {
            playSFXButton();
            intent = new Intent(this, activity_tutorial.class);
        } else if (viewId == R.id.btn_settings) {
            playSFXButton();
            intent = new Intent(this, activity_settings.class);
        } else if (viewId == R.id.btn_about_us) {
            playSFXButton();
            intent = new Intent(this, activity_about_us.class);
        } else {
            return; // Return early if the view ID is not recognized
        }
        startActivity(intent);
    }

    // Method to navigate back to MainActivity
    public void navigateToMainActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    public void playSFXButton() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isMutedSfx = prefs.getBoolean("isMutedSfx", false);
        // sfx_number calls the sfx track to be played, please view at SoundPlayer.class
        SoundPlayer.playSFX(this, isMutedSfx, 1);
        }


    public void exitApp(View view) {
        finishAffinity(); // Finish this activity and all activities immediately below it in the current task
    }

}
