package com.example.chenmin.alldemo.slidingtabstrip;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chenmin.alldemo.utils.ViewUtils;

import java.util.ArrayList;

/**
 * author : mathewchen
 * e-mail : 136214454@qq.com
 * time   : 2018/03/04
 * desc   : 滑动指示条
 * version: 1.0
 *
 * @author mathewchen
 */

public class SlidingTabStrip extends RelativeLayout {
    public interface TabOnClickListener {
        /**
         * tab 点击方法
         *
         * @param currentPosition 当前位置
         * @param v               视图
         */
        void onTabClick(int currentPosition, View v);
    }


    //默认值定义开始

    private int DEFAULT_NOT_SELECT_TEXT_COLOR = 0xff969696;
    private int DEFAULT_SELECT_TEXT_COLOR = 0xff282828;

    private int DEFAULT_NOT_SELECT_TEXT_SIZE = 14;
    private int DEFAULT_SELECT_TEXT_SIZE = 15;

    private int DEFAULT_INDICATOR_HEIGHT = 1;
    private int DEFAULT_INDICATOR_WIDTH = 20;

    private int DEFAULT_INDICATOR_COLOR = 0xfff0141e;


    //默认值结束结束


    private LinearLayout tabsContainer;

    private View indicator;

    /**
     * 未选/选择时TAB文字颜色
     */
    private int mNotSelectTextColor = DEFAULT_NOT_SELECT_TEXT_COLOR;
    private int mSelectTextColor = DEFAULT_SELECT_TEXT_COLOR;

    /**
     * 未选/选择时TAB文字字号大小
     */
    private int mNotSelectTextSize = DEFAULT_NOT_SELECT_TEXT_SIZE;
    private int mSelectTextSize = DEFAULT_SELECT_TEXT_SIZE;

    /**
     * 滑动指示器高度/宽度，默认是跟每个小TAB的宽度一致
     */
    private int mIndicatorHeight = DEFAULT_INDICATOR_HEIGHT;
    private int mIndicatorWidth = DEFAULT_INDICATOR_WIDTH;

    /**
     * 滑动指示器默认颜色
     */
    private int mIndicatorColor = DEFAULT_INDICATOR_COLOR;

    /**
     * 滑动指示器是否从底部开始放置
     */
    private boolean mIndicatorBottomIsParent = true;

    /**
     * 滑动指示器上下左右边距，注意，当mIndicatorBottomIsParent为真时底部边距才生效，为假时顶部边距才生效
     */
    private int mIndicatorBottomMargin = 0;
    private int mIndicatorRightMargin = 0;
    private int mIndicatorTopMargin = 0;
    private int mIndicatorLeftMargin = 0;

    private int tabMarginLeft = 16;
    private int tabMarginRight = 8;
    private int tabMarginTop = 0;
    private int tabMarginBottom = 0;

    private int currentPosition = 0;

    private TabOnClickListener tabOnClickListener;

    private ArrayList<TextView> tabList = new ArrayList<>();

    private ObjectAnimator animator;

    private boolean isGradient = false;
    private int mGradientStartColor = 0;
    private int mGradientEndColor = 0;

    private ViewPager mViewPager;

    private int mSingleDistance = 0;

    public SlidingTabStrip(Context context) {
        super(context);
        initView(context);
    }

    public SlidingTabStrip(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SlidingTabStrip(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //用一个内部容器去装载子tab
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        addView(tabsContainer);

        //初始化滑动指示器
        indicator = new View(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewUtils.dp2px(context, mIndicatorWidth),
                ViewUtils.dp2px(context, mIndicatorHeight));
        indicator.setBackgroundColor(mIndicatorColor);
        indicator.setLayoutParams(layoutParams);

        LayoutParams margin = new LayoutParams(indicator.getLayoutParams());
        margin.topMargin = ViewUtils.dp2px(context, mIndicatorTopMargin);
        margin.leftMargin = ViewUtils.dp2px(context, mIndicatorLeftMargin);
        margin.rightMargin = ViewUtils.dp2px(context,mIndicatorRightMargin);
        margin.bottomMargin = ViewUtils.dp2px(context,mIndicatorBottomMargin);

        if (mIndicatorBottomIsParent){
            margin.addRule(ALIGN_PARENT_BOTTOM);
        }

        addView(indicator, margin);

        animator = ObjectAnimator.ofFloat(indicator, "translationX", 0).setDuration(300);
    }

    public void addTextTab(final int position, String title) {

        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        tab.setTextColor(mNotSelectTextColor);
        tab.setTextSize(mNotSelectTextSize);
        if (position == currentPosition) {
            tab.setTextColor(mSelectTextColor);
            tab.setTextSize(mSelectTextSize);
            TextPaint tp = tab.getPaint();
            tp.setFakeBoldText(true);
            if (isGradient) {
                tab.setText(getRadiusGradientSpan(title, mGradientStartColor, mGradientEndColor));
            }
        }
        tab.setTag(position);

        addTab(position, tab);
    }

    /**
     * 刷新滑动指示器布局信息，主要是宽度要与子tab的宽度一致，在这个基础上进行左右Margin
     */
    private void refreshIndicatorLayout(int position){
        if (tabList.size() != 0) {
            //因为拿不到父布局的高度，所以match测量没有意义，直接取屏幕宽度
            WindowManager wm = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            int screenWidth = 0;
            if (wm != null) {
                screenWidth = wm.getDefaultDisplay().getWidth();
            }

            measure(0,0);
            int width = getWidth();
            Log.d("mathewchen","getWidth:"+width);

            int real = (px2dip(getContext(), screenWidth) - (tabMarginRight + tabMarginLeft) * tabList.size()) / tabList.size() / 2 + tabMarginLeft - mIndicatorWidth / 2;
            LayoutParams margin = new LayoutParams(indicator.getLayoutParams());
            margin.topMargin = ViewUtils.dp2px(getContext(), mIndicatorTopMargin);
            margin.leftMargin = ViewUtils.dp2px(getContext(), real);
            indicator.setLayoutParams(margin);
        }
//        if (position<tabList.size()){
//            TextView textView = tabList.get(0);
//            int width = textView.getWidth();
//            Log.d("mathewchen","getWidth:"+width);
//            LayoutParams margin = new LayoutParams(indicator.getLayoutParams());
//            margin.leftMargin = ViewUtils.dp2px(getContext(), tabMarginLeft);
//            indicator.setLayoutParams(margin);
//        }
    }

    private void addTab(final int position, final TextView tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animator.isRunning()) {
                    return;
                }
                int position = (int) v.getTag();
                updateText(position);
                int distance = (position) * (ViewUtils.dp2px(getContext(), tabMarginLeft + tabMarginRight) + v.getMeasuredWidth());
                animator = ObjectAnimator.ofFloat(indicator, "translationX", distance).setDuration(300);
                animator.start();
                currentPosition = position;
                if (tabOnClickListener != null) {
                    tabOnClickListener.onTabClick(currentPosition, v);
                }
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(currentPosition, true);
                }
            }
        });

        tabList.add(position, tab);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(ViewUtils.dp2px(getContext(), tabMarginLeft), ViewUtils.dp2px(getContext(), tabMarginTop), ViewUtils.dp2px(getContext(), tabMarginRight), ViewUtils.dp2px(getContext(), tabMarginBottom));
        layoutParams.weight = 1;
        tabsContainer.addView(tab, position, layoutParams);

    }


    private void updateText(int position) {
        for (TextView textView : tabList) {
            textView.setTextSize(mNotSelectTextSize);
            textView.setTextColor(mNotSelectTextColor);
            TextPaint tp = textView.getPaint();
            tp.setFakeBoldText(false);
            //清除颜色信息
            textView.setText(textView.getText().toString());
        }
        TextView textView = tabList.get(position);
        textView.setTextColor(mSelectTextColor);
        textView.setTextSize(mSelectTextSize);
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
        if (isGradient) {
            textView.setText(getRadiusGradientSpan(textView.getText().toString(), mGradientStartColor, mGradientEndColor));
        }
    }

    public void addTab(ArrayList<String> list) {
        int count = list.size();
        for (int i = 0; i < count; i++) {
            addTextTab(i, list.get(i));
        }
        refreshIndicatorLayout(0);
    }

    public void setTabOnClickListener(TabOnClickListener listener) {
        this.tabOnClickListener = listener;
    }

    public void removeAllTab() {
        tabList.clear();
        tabsContainer.removeAllViews();
    }

    private int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private SpannableStringBuilder getRadiusGradientSpan(String string, int startColor, int endColor) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(string);
        LinearGradientFontSpan span = new LinearGradientFontSpan(startColor, endColor);
        spannableStringBuilder.setSpan(span, 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 设置选中态文字上下渐变的颜色
     *
     * @param startColor 开始颜色
     * @param endColor   结束颜色
     */
    public void setSelectGradient(int startColor, int endColor) {
        isGradient = true;
        mGradientStartColor = startColor;
        mGradientEndColor = endColor;
    }

    public void setmSelectTextColor(int mSelectTextColor) {
        this.mSelectTextColor = mSelectTextColor;
    }

    public void setmUnselectColor(int mUnselectColor) {
        this.mNotSelectTextColor = mUnselectColor;
    }

    public void attachToPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (animator.isRunning()) {
                    return;
                }
                if (mSingleDistance == 0) {
                    mSingleDistance = (ViewUtils.dp2px(getContext(), tabMarginLeft + tabMarginRight) + tabList.get(0).getMeasuredWidth());
                }
                if (position == currentPosition) {
                    //左滑过程与右滑终点
                    indicator.setTranslationX(currentPosition * mSingleDistance + mSingleDistance * positionOffset);
                } else if (position > currentPosition) {
                    //左滑终点
                    indicator.setTranslationX(position * mSingleDistance);

                }else {
                    //右滑过程
                    indicator.setTranslationX(position * mSingleDistance + mSingleDistance * positionOffset);
                }


            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                updateText(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setIndicatorRes(int res) {
        indicator.setBackgroundResource(res);
    }

    public void setIndicatorHeightAndWidth(int width, int height) {
        this.mIndicatorHeight = height;
        this.mIndicatorWidth = width;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewUtils.dp2px(getContext(), mIndicatorWidth),
                ViewUtils.dp2px(getContext(), mIndicatorHeight));
        indicator.setLayoutParams(layoutParams);
        LayoutParams margin = new LayoutParams(indicator.getLayoutParams());
        margin.topMargin = ViewUtils.dp2px(getContext(), mIndicatorTopMargin);
        margin.leftMargin = ViewUtils.dp2px(getContext(), mIndicatorLeftMargin);
        indicator.setLayoutParams(margin);
    }
}
