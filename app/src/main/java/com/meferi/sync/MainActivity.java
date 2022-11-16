package com.meferi.sync;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_scan).setOnTouchListener(this);
        findViewById(R.id.camera_scan).setOnTouchListener(this);
        findViewById(R.id.setting).setOnTouchListener(this);


        findViewById(R.id.view_expert).setOnClickListener(this);
        findViewById(R.id.btn_scan).setOnClickListener(this);
        findViewById(R.id.camera_scan).setOnClickListener(this);
        findViewById(R.id.setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.view_expert:
                startActivity(new Intent(this, ExpertActivity.class));
                break;
            case R.id.btn_scan:
                break;
            case R.id.camera_scan:
                break;
            case R.id.setting:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        return false;
    }
}