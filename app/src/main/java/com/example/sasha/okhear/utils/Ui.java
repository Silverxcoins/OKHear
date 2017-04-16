package com.example.sasha.okhear.utils;

import android.os.Handler;
import android.os.Looper;

public class Ui {

    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void post(Runnable runnable) {
        handler.post(runnable);
    }
}
