package com.example.sasha.okhear.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class BarsUtil {

    public static void hideStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(uiVisibility | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static void setupFullscreenActivity(Activity activity) {
        BarsUtil.setStatusBarColor(activity, Color.TRANSPARENT);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static int getStatusBarHeight(Context context) {
        return (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
    }

    private static void setStatusBarColor(Activity activity, int color) {
        activity.getWindow().setStatusBarColor(color);
    }

}