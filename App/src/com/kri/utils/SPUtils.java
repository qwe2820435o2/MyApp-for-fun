package com.kri.utils;

import com.kri.config.Constants;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 
 * TODO<对Sharedpreferences功能的封装>
 * @author  LuLiLi
 * @data:  2016-3-19 下午10:08:35
 * @version:  V1.0
 */
public class SPUtils {
	/**
	 * @param context
	 * @param key
	 * 		关键字
	 * @param value
	 * 		值
	 */
	public static void putBoolean(Context context,String key,boolean value){
		SharedPreferences sp = context.getSharedPreferences(Constants.SPFILENAME, Context.MODE_PRIVATE);
		//添加保存数据
		sp.edit().putBoolean(key, value).commit();
		
	}
	
	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sp = context.getSharedPreferences(Constants.SPFILENAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
		
	}
	
	public static void putString(Context context,String key,String value){
		SharedPreferences sp = context.getSharedPreferences(Constants.SPFILENAME, Context.MODE_PRIVATE);
		//添加保存数据
		sp.edit().putString(key, value).commit();
		
	}
	
	public static String getString(Context context,String key,String defValue){
		SharedPreferences sp = context.getSharedPreferences(Constants.SPFILENAME, Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
		
	}
	public static void putInt(Context context,String key,int value){
		SharedPreferences sp = context.getSharedPreferences(Constants.SPFILENAME, Context.MODE_PRIVATE);
		//添加保存数据
		sp.edit().putInt(key, value).commit();
		
	}
	
	public static int getInt(Context context,String key,int defValue){
		SharedPreferences sp = context.getSharedPreferences(Constants.SPFILENAME, Context.MODE_PRIVATE);
		return sp.getInt(key, defValue);
		
	}
	public static void putLong(Context context,String key,long value){
		SharedPreferences sp = context.getSharedPreferences(Constants.SPFILENAME, Context.MODE_PRIVATE);
		//添加保存数据
		sp.edit().putLong(key, value).commit();
		
	}
	
	public static long getLong(Context context,String key,Long defValue){
		SharedPreferences sp = context.getSharedPreferences(Constants.SPFILENAME, Context.MODE_PRIVATE);
		return sp.getLong(key, defValue);
		
	}
}
