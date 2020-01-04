package com.example.mediaplayerapplication.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.mediaplayerapplication.R;
import com.example.mediaplayerapplication.util.Constants;
import com.example.mediaplayerapplication.util.Logger;

public class PlayService extends Service {

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        Logger.logString("Service on Create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        Logger.logString("Service onStartCommand");
        sendActivityMessage(Constants.SERVICE_PLAYING);
        return super.onStartCommand(intent, flags, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.logString("Service Bind");
        return new PlayServiceBinder();
    }

    public void playMedia(){
        if(!mediaPlayer.isPlaying())
            mediaPlayer.start();

        sendActivityMessage(Constants.SERVICE_PLAYING);
    }

    public void pauseMedia(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        sendActivityMessage(Constants.SERVICE_PAUSED);
    }

    public void stopMedia(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(this, R.raw.sound);
            mediaPlayer.setLooping(true);

        sendActivityMessage(Constants.SERVICE_STOPPED);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return super.onUnbind(intent);
    }

    private void sendActivityMessage(String message) {
        Intent broadcastIntent = new Intent(Constants.SERVICE_ACTION);
        broadcastIntent.putExtra(Constants.SERVICE_ACTION, message);
        sendBroadcast(broadcastIntent);
    }


    public class PlayServiceBinder extends Binder {
        public PlayService getPlayServices(){
            return PlayService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        Logger.logString("Service Destroyed");
    }

}
