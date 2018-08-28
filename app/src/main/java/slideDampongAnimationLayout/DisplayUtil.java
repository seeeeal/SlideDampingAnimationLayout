package slideDampongAnimationLayout;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Copyright (C) 2018 Unicorn, Inc.
 * Description :
 * Created by dabutaizha on 2018/7/20 下午3:31.
 */

public class DisplayUtil {

    private Context mContext;
    private static DisplayUtil mInstance;

    public static DisplayUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DisplayUtil.class) {
                if (mInstance == null) {
                    mInstance = new DisplayUtil(context);
                }
            }
        }
        return mInstance;
    }

    private DisplayUtil(Context context) {
        mContext = context;
    }

    /**
     *Description: 获取屏幕高度
     */
    public int getScreenHeight() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     *Description: 获取屏幕宽度
     */
    public int getScreenWidth() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     *Description: dp转px
     */
    public float dp2px(final float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

}
