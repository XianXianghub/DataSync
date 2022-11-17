package com.meferi.sync;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnTouchListener, View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.camera_scan, R.id.view_expert, R.id.setting})
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