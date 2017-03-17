package com.example.sasha.okhear;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_start)
public class StartActivity extends AppCompatActivity {

    @AfterViews
    void init() {
        Intent intent = new Intent(this, MainActivity_.class);
        startActivity(intent);
    }
}
