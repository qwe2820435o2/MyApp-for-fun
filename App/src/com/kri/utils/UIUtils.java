package com.kri.utils;

import com.kri.base.MyApplication;

import android.content.Context;
import android.content.res.Resources;
import android.os.*;


/*
 * @创建者     LuLiLi
 * @创建时间   2016/2/1 14:19
 * @描述	      ${提供各种工具，快速开发}
 *
 * @更新者     $Author$
 * @更新时间   $Date$
 * @更新描述   ${TODO}
 */
public class UIUtils {
    //获取上下文
    public static Context getContext() {
        return MyApplication.getContext();
    }

    //得到Resouce
    public static Resources getResources() {
        return getContext().getResources();
    }

    //得到String.xml文件中的字符串
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    //得到string.xml的一些字符串，带占位符的情况
    public static String getString(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    //得到string.xml中的一些字符串数组
    public static String[] getStringArr(int resId) {
        return getResources().getStringArray(resId);
    }

    //得到color.xml中的一些字符串数组
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    //得到当前的包名
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    //得到主线程的Id
    public static long getMainThreadId() {
        return MyApplication.getMainThreadId();
    }

    //在主线程中创建一个Handler
    public static Handler getMainHandler() {
        return MyApplication.getHandler();
    }

    //安全地执行一个task
    public static void postTaskSafely(Runnable task) {
        //得到当前线程的id
        int curThreadID = android.os.Process.myTid();
        //得到主线程的id
        long mainThreadId = getMainThreadId();

        if (curThreadID == mainThreadId) {
            //如果在主线程，直接执行
            task.run();
        } else {
            //如果在子线程，把该任务交给主线程的handler执行
            getMainHandler().post(task);
        }
    }

    //dp-->px
    public static int dip2Px(int dp) {
        float density = getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + .5f);
        return px;
    }

    //px-->dp
    public static int px2Dp(int px) {
        int density = getResources().getDisplayMetrics().densityDpi;
        int dp = (int) (px / density + .5f);
        return dp;
    }

    //延迟执行任务
    public static void postTaskDelay(Runnable task, long delayMillis) {
        UIUtils.getMainHandler().postDelayed(task, delayMillis);
    }

    //移除一个任务
    public static void removeTask(Runnable task){
        UIUtils.getMainHandler().removeCallbacks(task);
    }
}

