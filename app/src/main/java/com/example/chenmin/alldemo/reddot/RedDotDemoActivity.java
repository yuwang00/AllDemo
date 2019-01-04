package com.example.chenmin.alldemo.reddot;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.chenmin.alldemo.R;

public class RedDotDemoActivity extends Activity{
    private EditText mEdit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_dot);
        initView();
    }

    private void initView(){
        final RedPointView redPointView = findViewById(R.id.rd_red_dot);
        redPointView.show("");

        mEdit = findViewById(R.id.edit);
        final TextView showTv = findViewById(R.id.tv_show);
        showTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redPointView.show(mEdit.getText().toString());
            }
        });

        TextView disTv = findViewById(R.id.tv_dismiss);
        disTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redPointView.dismiss();
            }
        });
    }
}
