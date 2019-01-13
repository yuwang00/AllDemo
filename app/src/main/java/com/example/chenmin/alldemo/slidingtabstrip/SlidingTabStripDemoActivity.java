package com.example.chenmin.alldemo.slidingtabstrip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.example.chenmin.alldemo.R;

import java.util.ArrayList;

/**
 * author : chenmin
 * e-mail : 136214454@qq.com
 * time   : 2019/01/13
 * desc   :
 * version: 1.0
 *
 * @author chenmin
 */
public class SlidingTabStripDemoActivity extends AppCompatActivity {

    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_tab_strip);
        initData();
        initView();
    }

    private void initData(){
        mTitles.add("Fragment1");
        mTitles.add("Fragment2");
    }

    private void initView(){
        ViewPager viewPager = findViewById(R.id.viewpager);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(),mTitles);
        viewPager.setAdapter(adapter);

        SlidingTabStrip mTabs = findViewById(R.id.tabs);
//        mTabs.setSelectGradient(CustomizedPlayConstant.TAB_TEXT_START_COLOR, CustomizedPlayConstant.TAB_TEXT_END_COLOR);
        mTabs.setmUnselectColor(0xffffffff);
        mTabs.setmSelectTextColor(0xffFF0033);
//        mTabs.setIndicatorHeightAndWidth(CustomizedPlayConstant.TAB_INDICATOR_WIDTH, CustomizedPlayConstant.TAB_INDICATOR_HEIGHT);
//        mTabs.setIndicatorRes(R.drawable.bg_indicator);
        mTabs.addTab(mTitles);

        mTabs.attachToPager(viewPager);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<String> cateArrayList;
        private SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();

        MyPagerAdapter(FragmentManager fm, ArrayList<String> titles) {
            super(fm);
            this.cateArrayList = titles;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = fragmentSparseArray.get(position);
            if (fragment == null) {
                fragment = new DemoFragment();
                ((DemoFragment) fragment).setTitle(getPageTitle(position));
                fragmentSparseArray.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return cateArrayList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return cateArrayList.get(position);
        }
    }
}
