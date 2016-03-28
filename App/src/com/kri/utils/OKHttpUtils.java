package com.kri.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * 
 * TODO<OKHttp封装工具类>
 * @author  LuLiLi
 * @data:  2016-2-17 下午11:08:02
 * @version:  V1.0
 */
public class OKHttpUtils {

	
	private static OKHttpUtils mOKHttpUtils;
	private OkHttpClient mOkHttpClient;

	public static OKHttpUtils getInstance() {
		if (null == mOKHttpUtils) {
			mOKHttpUtils = new OKHttpUtils();
		}
		return mOKHttpUtils;
	}

	private OKHttpUtils() {
		mOkHttpClient = new OkHttpClient();
		mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
	}
	
	/**
	 * 
	 * TODO<不开启异步线程>
	 * @param request
	 * @return
	 * @throws IOException
	 * @throw
	 * @return Response
	 */
	public  Response execute(Request request) throws IOException {
		return mOkHttpClient.newCall(request).execute();
	}

	/**
	 * 
	 * TODO<开启异步线程访问，访问结果自行处理>
	 * @param request
	 * @param responseCallback
	 * @throw
	 * @return void
	 */
	public  void enqueue(Request request, Callback responseCallback) {
		mOkHttpClient.newCall(request).enqueue(responseCallback);
	}
	
	/**
	 * 
	 * TODO<开启异步线程访问,不对访问结果进行处理>
	 * @param request
	 * @throw
	 * @return void
	 */
	public  void enqueue(Request request) {
		mOkHttpClient.newCall(request).enqueue(new Callback() {
			
			@Override
			public void onResponse(Response arg0) throws IOException {
			}
			
			@Override
			public void onFailure(Request arg0, IOException arg1) {
			}
		});
	}

	/**
	 * 
	 * TODO<为HttpGet请求拼接一个参数>
	 * @param url
	 * @param name
	 * @param value
	 * @return
	 * @throw
	 * @return String
	 */
	public  String jointURL(String url, String name, String value) {
		return url + "?" + name + "=" + value;
	}

	/**
	 * 
	 * TODO<为HttpGet请求拼接多个参数>
	 * @param url
	 * @param values
	 * @return
	 * @throw
	 * @return String
	 */
	public  String jointURL(String url, Map<String, String> values) {
		StringBuffer result = new StringBuffer();
		result.append(url).append("?");
		Set<String> keys = values.keySet();
		for (String key : keys) {
			result.append(key).append("=").append(values.get(key)).append("&");
		}
		return result.toString().substring(0, result.toString().length()-1);
	}
	
}
