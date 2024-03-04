package com.example.mainmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
public class activity_start_game extends AppCompatActivity implements pause_dialog.DialogCallback {

    private TextView timerCount, scoreCount;
    private CountDownTimer cTimer;
    private long mTimeLeftInMills = 90000;
    private int runningScore = 0, pickupScore = 0, totalScore = 0;
    private boolean isMuted, isMutedSFX;
    private boolean gamePaused = false;
    private pause_dialog pauseDialog;

    private ImageView roadImageView1;
    private ImageView roadImageView2;

    private Animation roadAnimation1;
    private Animation roadAnimation2;

    private long animationDuration = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        // Start timer
        startTimer();

        // Start background music
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        isMuted = prefs.getBoolean("isMuted", false);
        isMutedSFX = prefs.getBoolean("isMutedSfx", false);

        if (!isMuted) {
            SoundPlayer.playBGM(this);
        }

        // Initialize road animations
        roadImageView1 = findViewById(R.id.roadImageView1);
        roadImageView2 = findViewById(R.id.roadImageView2);

        roadAnimation1 = AnimationUtils.loadAnimation(this, R.anim.translate_animation);
        roadAnimation2 = AnimationUtils.loadAnimation(this, R.anim.translate_animation);

        // Set custom AnimationListeners for both animations
        roadAnimation1.setAnimationListener(new MyAnimationListener(roadImageView2));
        roadAnimation2.setAnimationListener(new MyAnimationListener(roadImageView1));

        // Set the new duration for both animations
        roadAnimation1.setDuration(animationDuration);
        roadAnimation2.setDuration(animationDuration);

        // Start the road animations for each ImageView
        roadImageView1.startAnimation(roadAnimation1);
        roadImageView2.startAnimation(roadAnimation2);


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
                if (!gamePaused) {
                    // If the timer is running, pause it
                    pauseTimer();
                    SoundPlayer.pauseBGM();
                    gamePaused = true;
                    // Show pause dialog
                    pauseDialog = new pause_dialog(activity_start_game.this, activity_start_game.this);
                    pauseDialog.show();
                    btnPause.setEnabled(false); // Disable pause button
                } else {
                    // If the timer is paused, resume it
                    startTimer();
                    gamePaused = false;
                    // Dismiss pause dialog if it's showing
                    if (pauseDialog != null && pauseDialog.isShowing()) {
                        pauseDialog.dismiss();
                    }
                    SoundPlayer.playBGM(activity_start_game.this);
                    btnPause.setEnabled(true); // Re-enable pause button
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

    @Override
    protected void onResume() {
        super.onResume();
        // Enable pause button when the activity is resumed
        Button btnPause = findViewById(R.id.btn_pause);
        btnPause.setEnabled(true);
    }

    private void startTimer() {
        cTimer = new CountDownTimer(mTimeLeftInMills, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMills = millisUntilFinished;
                updateScore();
                updateCountDownText();
            }

            @Override
            public void onFinish() {
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

    private void addScore() {
        pickupScore += 250;
    }

    private void updateScore() {
        if (mTimeLeftInMills >= 61000) {
            runningScore += 1;
        } else if (mTimeLeftInMills >= 31000) {
            runningScore += 5;
        } else {
            runningScore += 10;
        }

        runningScore = Math.min(runningScore, 4800);

        totalScore = runningScore + pickupScore;
        String formattedScore = String.format("%06d", totalScore);
        scoreCount.setText(formattedScore);
    }

    @Override
    public void onBooleanPassed(boolean value, boolean value2) {
        if (value) {
            startTimer();
            SoundPlayer.playBGM(this);
            gamePaused = false;
        }

        SoundPlayer.muteVolume(value2);
        isMutedSFX = value2;

        Button btnPause = findViewById(R.id.btn_pause);
        btnPause.setEnabled(true);
    }

    private static class MyAnimationListener implements Animation.AnimationListener {
        private final ImageView imageView;

        MyAnimationListener(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            // Do nothing on start
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // Reset the position of the ImageView to create a seamless loop
            imageView.clearAnimation();
            imageView.startAnimation(animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // Do nothing on repeat
        }
    }
}
