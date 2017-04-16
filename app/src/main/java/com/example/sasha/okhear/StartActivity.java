package com.example.sasha.okhear;

import android.app.Activity;
import android.content.Intent;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.activity_start)
public class StartActivity extends Activity {
    @AfterViews
    protected void init() {
        startActivity(new Intent(this, MainActivity_.class));
    }
}
