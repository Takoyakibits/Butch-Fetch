package com.example.mainmenu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class pause_dialog extends Dialog {
    private Button btnMute;
    private Button btnGoBackToMenu;
    private Button btnClose;

    private boolean isMuted = false; // Track mute state
    private boolean isPaused = false; // Track pause state
    private DialogCallback callback;

    public pause_dialog(Context context, DialogCallback callback) {
        super(context);
        this.callback = callback;

        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        isMuted = prefs.getBoolean("isMuted", false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set dialog window properties
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setContentView(R.layout.dialog_pause);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        // Initialize buttons
        btnMute = findViewById(R.id.btn_mute);
        btnGoBackToMenu = findViewById(R.id.btn_go_back_to_menu);
        btnClose = findViewById(R.id.btn_close);

        // Set click listeners
        btnMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMute(); // Toggle mute state
            }
        });

        btnGoBackToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundPlayer.stopBGM();
                // Handle go back to menu button click
                // Implement the logic to navigate back to the MainActivity
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                getContext().startActivity(intent);
                dismiss(); // Dismiss the dialog after performing the action
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle pause state
                isPaused = !isPaused;
                // Dismiss the dialog to continue the game
                sendBooleanToActivity(isPaused, isMuted);
                dismiss();
            }
        });

        updateMuteButtonBackground(isMuted);
    }

    private void toggleMute() {
        isMuted = !isMuted; // Toggle mute state
        // Set mute state using settingManager
        SharedPreferences prefs = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isMuted", isMuted);
        editor.putBoolean("isMutedSfx", isMuted);
        editor.apply();
        // Update button background based on new mute state
        updateMuteButtonBackground(isMuted);
    }

    private void updateMuteButtonBackground(boolean isMuted) {
        if (!isMuted) {
            btnMute.setBackgroundResource(R.drawable.soundon);
        } else {
            btnMute.setBackgroundResource(R.drawable.soundoff);
        }
    }

    public interface DialogCallback {
        void onBooleanPassed(boolean isPaused, boolean isMuted);
    }

    private void sendBooleanToActivity(boolean isPaused, boolean isMuted) {
        if (callback != null) {
            callback.onBooleanPassed(isPaused, isMuted);
        }
    }
}
