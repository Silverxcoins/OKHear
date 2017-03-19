package com.example.sasha.okhear.utils;

import android.view.View;

public class Utils {
    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }
}
