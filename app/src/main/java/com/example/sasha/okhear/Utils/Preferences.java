package com.example.sasha.okhear.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class Preferences {

    public static final String SPEAK_OR_SHOW_KEY = "speakOrShow";
    public static final int SPEAK = 0;
    public static final int SHOW = 1;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private volatile int speakOrShow;

    @SuppressLint("CommitPrefEdits")
    public Preferences(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
        speakOrShow = sharedPreferences.getInt(SPEAK_OR_SHOW_KEY, SHOW);
    }

    public void setSpeakOrShow(int speakOrShow) {
        this.speakOrShow = speakOrShow;
        editor.putInt(SPEAK_OR_SHOW_KEY, speakOrShow);
        editor.apply();
    }

    public void changeSpeakOrShow() {
        speakOrShow = (speakOrShow  == SPEAK ? SHOW : SPEAK);
        editor.putInt(SPEAK_OR_SHOW_KEY, speakOrShow);
        editor.apply();
    }

    public int getSpeakOrShow() {
        return speakOrShow;
    }
}
