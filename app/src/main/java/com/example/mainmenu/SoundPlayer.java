package com.example.mainmenu;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundPlayer {
    private static MediaPlayer player;
    private static SoundPool soundPool;
    private static int sfx1, sfx2, sfx3, sfx4, sfx5;
    private static int length;

    private static AudioAttributes aa = new AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build();
    public static void initialize(Context context) {
        if (soundPool == null) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(aa)
                    .build();
            sfx1 = soundPool.load(context, R.raw.buttonpress, 1);
            sfx2 = soundPool.load(context, R.raw.incoming_warning, 2);
            sfx3 = soundPool.load(context, R.raw.trashcan_hit, 3);
            sfx4 = soundPool.load(context, R.raw.powerup_crunch, 4);
            sfx5 = soundPool.load(context, R.raw.car_crash, 5);
        }
    }
    public static void playBGM(Context context){
            player = MediaPlayer.create(context, R.raw.runningbgm1);
            player.seekTo(length);
            player.start();
    }

    public static void pauseBGM(){
        if(player != null) {
            if (player.isPlaying()) {
                player.pause();
                length = player.getCurrentPosition();
            }
        }
    }
    public static void stopBGM(){
        if(player != null) {
            player.stop();
            length = 0;
        }
    }

    public static void muteVolume(boolean isMute){

        if(isMute) {
            if (player != null) {
                player.setVolume(0f, 0f);
            }
        }else{
            if (player != null) {
                player.setVolume(1f, 1);
            }
        }
    }

    public static void playSFX(Context context, boolean isMute, int sfx_number) {
        if (soundPool == null) {
            initialize(context);
        }
        if(!isMute) {
            soundPool.play(sfx_number, 1, 1, 1, 0, 1);
        }else{
            soundPool.play(sfx_number, 0, 0, 1, 0, 1);

        }
    }
}

