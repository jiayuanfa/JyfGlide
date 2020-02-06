package com.example.jyfglide;

import android.content.Context;

/**
 * 给程序员提供一个总的调用方法
 */
public class JyfGlide {

    /**
     * 总的方法
     * @param context
     * @return
     */
    public static BitmapRequest with(Context context) {
        return new BitmapRequest(context);
    }
}
