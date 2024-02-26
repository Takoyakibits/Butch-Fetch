package com.example.mainmenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class activity_start_game extends AppCompatActivity implements pause_dialog.DialogCallback {

    private TextView timerCount, scoreCount;
    private CountDownTimer cTimer;
    // Starting time = 90 seconds
    private static final int START_TIME_IN_MILLIS = 90000;
    // Initialize the time left
    private long mTimeLeftInMills = START_TIME_IN_MILLIS;
    private int runningScore = 0, pickupScore = 0, totalScore = 0;
    private boolean isMuted, isMutedSFX;
    private boolean gamePaused = false;
    // Declare pause dialog variable
    private pause_dialog pauseDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        // Start timer (please adjust placement)
        startTimer();

        // Start background music
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        isMuted = prefs.getBoolean("isMuted", false);
        isMutedSFX = prefs.getBoolean("isMutedSfx", false);

        if (!isMuted) {
            SoundPlayer.playBGM(this);
        }

        // Example: Displaying a message in a TextView
        TextView textView = findViewById(R.id.textView);
        textView.setText("Game Started!");

        // Call timer
        timerCount = findViewById(R.id.countText);
        scoreCount = findViewById(R.id.scoreText);

        updateCountDownText();

        // Pause Button
        Button btnPause = findViewById(R.id.btn_pause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cTimer != null) {
                    SoundPlayer.playSFX(activity_start_game.this, isMutedSFX, 1 );
                    // If the timer is running, pause it
                    pauseTimer();
                    SoundPlayer.pauseBGM();
                    // Show pause dialog
                    pauseDialog = new pause_dialog(activity_start_game.this, activity_start_game.this);
                    pauseDialog.show();
                } else {
                    // If the timer is paused, resume it
                    startTimer();
                    // Dismiss pause dialog if it's showing
                    if (pauseDialog != null && pauseDialog.isShowing()) {
                        pauseDialog.dismiss();
                    }
                    SoundPlayer.playBGM(activity_start_game.this);
                }
            }
        });


        // Initialize pause dialog
        pauseDialog = new pause_dialog(activity_start_game.this, activity_start_game.this);
        pauseDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!gamePaused) {
                    startTimer();
                }
            }
        });
    }

    private void startTimer() {
        cTimer = new CountDownTimer(mTimeLeftInMills, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                //
                mTimeLeftInMills = millisUntilFinished;
                updateScore();
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                // execute code when the timer is finished
                SoundPlayer.stopBGM();
                Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void pauseTimer() {
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

    private void updateCountDownText() {
        int seconds = (int) (mTimeLeftInMills / 1000);
        String timeleftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        timerCount.setText(timeleftFormatted);
    }

    // Score obtained from powerup
    private void addScore() {
        pickupScore += 250;
    }

    private void updateScore() {
        // Increment running score based on time left
        if (mTimeLeftInMills >= 61000) {
            runningScore += 1;
        } else if (mTimeLeftInMills >= 31000) {
            runningScore += 5;
        } else {
            runningScore += 10;
        }

        // Ensure that the running score does not exceed the maximum limit
        runningScore = Math.min(runningScore, 4800);

        // Calculate total score and format it, then set it to the score textView
        totalScore = runningScore + pickupScore;
        String formattedScore = String.format("%06d", totalScore);
        scoreCount.setText(formattedScore);
    }
    @Override
    public void onBooleanPassed(boolean value, boolean value2) {
        // Returns true after closing the pause_dialog, used to resume the game
        if (value) {
            startTimer();
            SoundPlayer.playBGM(this);
        }
        // value2 indicates the status of the sound icon in the pause_dialog
        SoundPlayer.muteVolume(value2);
        isMutedSFX = value2;
    }
}
