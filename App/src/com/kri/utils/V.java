package com.kri.utils;

import android.app.Activity;
import android.view.View;

/**
 * 
 * TODO<简化findViewById的工具类>
 * @author  NongJinLin
 * @data:  2016-1-10 下午2:38:49
 * @version:  V1.0
 */
public class V {

    /**
     * activity.findViewById()
     * @param context
     * @param id
     * @return
     */
    public static <T extends View> T f(Activity context, int id) {
        return (T) context.findViewById(id);
    }

    /**
     * rootView.findViewById()
     * @param rootView
     * @param id
     * @return
     */
    public static <T extends View> T f(View rootView, int id) {
        return (T) rootView.findViewById(id);
    }
}
