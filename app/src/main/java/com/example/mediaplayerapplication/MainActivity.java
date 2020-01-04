package com.example.mediaplayerapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mediaplayerapplication.service.MyIntentService;
import com.example.mediaplayerapplication.service.PlayService;
import com.example.mediaplayerapplication.util.Constants;
import com.example.mediaplayerapplication.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    Intent serviceIntent;

    private PlayService playService;

    @BindView(R.id.text_view)
    TextView mediaTextView;

    @BindView(R.id.play_button)
    ImageView playButtonImage;

    @BindView(R.id.pause_button)
    ImageView pauseButtonImage;

    @BindView(R.id.stop_button)
    ImageView stopButtonImage;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.logString("Service bound to. . .");
            playService = ((PlayService.PlayServiceBinder) service).getPlayServices();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //if unexpected service error/interuption occurs
        }
    };

    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(Constants.SERVICE_ACTION.equals(action)){
                switch (intent.getStringExtra(Constants.SERVICE_ACTION)){
                    case Constants.SERVICE_PLAYING:
                        mediaTextView.setText(Constants.SERVICE_PLAYING);
                        mediaTextView.setBackgroundColor(getColor(R.color.colorPlaying));
                        break;

                    case Constants.SERVICE_PAUSED:
                        mediaTextView.setText(Constants.SERVICE_PAUSED);
                        mediaTextView.setBackgroundColor(getColor(R.color.colorPaused));
                        break;

                    case Constants.SERVICE_STOPPED:
                        mediaTextView.setText(Constants.SERVICE_STOPPED);
                        mediaTextView.setBackgroundColor(getColor(R.color.colorStop));
                        break;
                }
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intentServiceExample = new Intent(this, MyIntentService.class);
        startService(intentServiceExample);

        ButterKnife.bind(this);
        serviceIntent = new Intent(this, PlayService.class);
        startService(serviceIntent);
        //At this point the service has started.

        bindService(serviceIntent, serviceConnection, Service.BIND_AUTO_CREATE);

        registerReceiver(serviceReceiver, new IntentFilter(Constants.SERVICE_ACTION));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(serviceReceiver);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(serviceIntent);
    }

    @OnClick(R.id.play_button)
    public void onPlayMedia(){
        playService.playMedia();
    }

    @OnClick(R.id.pause_button)
    public void onPauseMedia(){
        playService.pauseMedia();
    }

    @OnClick(R.id.stop_button)
    public void onStopMedia(){
        playService.stopMedia();
    }
}
