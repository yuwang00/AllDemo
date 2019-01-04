package com.example.chenmin.alldemo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;


import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Copyright © 2018 tencent. All rights reserved.
 *
 * @author: mathewchen
 * @Description:
 * @date: 2018/08/07
 */
public class ViewUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        if (context == null) {
            return (int) (dpValue * 2.75 + 0.5f);
        }

        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static boolean isNavigationBarShow(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }

    }

    /**
     * 隐藏键盘
     */
    public static boolean hideSoftKeyboard(Context context, View view) {

        if (context != null && context instanceof Activity) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);

            if (imm != null && imm.isActive()) {
                return imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        }
        return false;

    }
}
