package com.example.sasha.okhear.Utils;

import android.app.Activity;

public class StatusBarUtils {

    public static void setStatusBarColor(Activity activity, int color) {
        activity.getWindow().setStatusBarColor(color);
    }

}
