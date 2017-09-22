package com.codegemz.elfi.coreapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class MusicService extends Service {
    public static final String ACTION_PLAY = "com.codegemz.elfi.coreapp.action.PLAY";

    public MusicService() {
    }
    MediaPlayer player;
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
        player = MediaPlayer.create(this, R.raw.ikonapop); //create a folder in res folder named as raw and add mp3 as song
        player.setLooping(false);
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        player.start();

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        if (player != null){
            player.stop();
            player.release();
        }

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}