package com.jacob.surface;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private LuckyPanView mLuckPanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_start).setOnClickListener(this);
        findViewById(R.id.button_stop).setOnClickListener(this);

        mLuckPanView = (LuckyPanView) findViewById(R.id.luckPan);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                mLuckPanView.startRotate();
                break;
            case R.id.button_stop:
                mLuckPanView.stopRotate();
                break;
        }
    }
}
