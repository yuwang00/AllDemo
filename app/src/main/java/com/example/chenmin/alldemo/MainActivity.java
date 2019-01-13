package com.example.chenmin.alldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chenmin.alldemo.reddot.RedDotDemoActivity;
import com.example.chenmin.alldemo.slidingtabstrip.SlidingTabStripDemoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mRedDotTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        mRedDotTv = findViewById(R.id.tv_red_dot);
        mRedDotTv.setOnClickListener(this);

        findViewById(R.id.tv_tab).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_red_dot:
                startActivity(new Intent(this, RedDotDemoActivity.class));
                break;
            case R.id.tv_tab:
                startActivity(new Intent(this, SlidingTabStripDemoActivity.class));
                break;
            default:
                break;
        }
    }
}
