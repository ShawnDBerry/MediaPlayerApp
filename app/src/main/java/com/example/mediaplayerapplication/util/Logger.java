package com.example.mediaplayerapplication.util;

import android.util.Log;

public class Logger {

    private static final String TAG = "TAG_Logger";

    public static void logString(String message) {
        Log.d(TAG, message);
    }

    public static void logError(String message) {
        Log.e(TAG, message );
    }
}
