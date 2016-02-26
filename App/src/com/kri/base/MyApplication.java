package com.kri.base;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.kri.utils.LogUtils;

public class MyApplication extends Application {
	private final String TAG = "MyApplication";

	private boolean isLastVersion = true;
	private static MyApplication instance;
	private static Context	mContext;//上下文
	private static Handler	mHandler;//主线程的handler
	private static long	mMainThreadId;//主线程Id
	
	//存放协议内容到内存的容器
	private Map<String,String> mProtocolCacheMap=new HashMap<String,String>();
	
	

	public Map<String, String> getProtocolCacheMap() {
		return mProtocolCacheMap;
	}

	// 全局Activity容器
	private List<Activity> mList = new LinkedList<Activity>();


	private float mDensity;

	private float mWidthPixels;

	private float mHeightPixels;

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		instance = this;
		//mOkHttpUtils = new OKHttpUtils();
		mContext=getApplicationContext();
		mHandler=new Handler();
		mMainThreadId=android.os.Process.myTid();
		
		mDensity = getResources().getDisplayMetrics().density;
		mWidthPixels = getResources().getDisplayMetrics().widthPixels;
		mHeightPixels = getResources().getDisplayMetrics().heightPixels;
		LogUtils.i(TAG, "density:" + mDensity + ",widthPixels:" + mWidthPixels
				+ ",heightPixels:" + mHeightPixels);
	}
	
	public static Context getContext() {
		return mContext;
	}

	public static Handler getHandler() {
		return mHandler;
	}

	public static long getMainThreadId() {
		return mMainThreadId;
	}
	
	public boolean getLastVersion() {
		return isLastVersion;
	}

	public void setLastVersion(boolean isLastVersion) {
		this.isLastVersion = isLastVersion;
	}
	
	public static MyApplication getApp(){
		return instance;
	}

	// 添加activity到集合
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	// 关闭某个activity
	public void finishActivity(Activity activity) {
		if (activity != null) {
			mList.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	//关闭所有的activity
	public void finishActivity() {
		for (Activity activity : mList) {
			if (activity!=null) {
				activity.finish();
			}
		}
	}
	
	
}
