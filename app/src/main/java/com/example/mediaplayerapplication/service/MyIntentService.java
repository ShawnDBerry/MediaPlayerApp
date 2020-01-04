package com.example.mediaplayerapplication.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.mediaplayerapplication.util.Logger;

public class MyIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService() {
        super("My Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Logger.logString(Thread.currentThread().getName());
        for(int i = 0; i<10; i++){
            try {
                Thread.sleep(1000);
                Logger.logString("Seconds " + (i + 1) + "/10");
            } catch (InterruptedException e) {
                Logger.logError(e.getMessage());
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.logString("Intent Service onDestroy");
    }
}
