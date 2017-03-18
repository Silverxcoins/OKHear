package com.example.sasha.okhear.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class StatusBarUtils {

    public static void setStatusBarColor(Activity activity, int color) {
        activity.getWindow().setStatusBarColor(color);
    }

    public static void hideStatusBar(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiVisibility = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(uiVisibility | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static void setupFullscreenActivity(Activity activity) {
        StatusBarUtils.setStatusBarColor(activity, Color.TRANSPARENT);
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static int getStatusBarHeight(Context context) {
        return (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);
    }

}
