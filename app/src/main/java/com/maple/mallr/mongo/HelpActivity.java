package com.maple.mallr.mongo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void tutorial(View view) {

        Intent intent1 = new Intent(this, MapsActivity.class);
        startActivity(intent1);

        Intent intent2 = new Intent(this, InfoActivity.class);
        startActivity(intent2);

    }
}
