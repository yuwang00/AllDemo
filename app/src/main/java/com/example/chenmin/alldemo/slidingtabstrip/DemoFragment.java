package com.example.chenmin.alldemo.slidingtabstrip;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chenmin.alldemo.R;

/**
 * author : chenmin
 * e-mail : 136214454@qq.com
 * time   : 2019/01/13
 * desc   :
 * version: 1.0
 *
 * @author chenmin
 */
public class DemoFragment extends Fragment {
    private CharSequence mTitle;

    public DemoFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_demo_fragment,container,false);
        initView(view);
        return view;
    }

    private void initView(View view){
        TextView textView = view.findViewById(R.id.textview);
        if (!TextUtils.isEmpty(mTitle)){
            textView.setText(mTitle);
        }
    }

    public void setTitle(CharSequence title){
        this.mTitle = title;
    }
}
