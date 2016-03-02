package com.kri.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.kri.config.Constants;
import com.kri.utils.FileUtils;
import com.kri.utils.HttpUtils;
import com.kri.utils.IOUtils;
import com.kri.utils.LogUtils;
import com.kri.utils.UIUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * 
 * TODO<对项目里的网络请求做封装>
 * @author  LuLiLi
 * @data:  2016-2-21 上午9:03:22
 * @version:  V1.0
 * @param <T>
 */
public abstract class BaseProtocol<T> {
	


	/**
	 * 
	 * TODO<进行网络请求的方法>
	 * @param index
	 * @return
	 * @throws IOException
	 * @throw
	 * @return T
	 */
	public T loadData(int index) throws IOException {
		//------------从内存读取------------------
		MyApplication app=(MyApplication) UIUtils.getContext();
		Map<String, String> protocolCacheMap = app.getProtocolCacheMap();
		
		//得到缓存的关键字
		String key=generateKey(index);
		
		T t=null;
		if (protocolCacheMap.containsKey(key)) {
			String  memJsonString = protocolCacheMap.get(key);
			LogUtils.e("debug", "从内存中取数据");
			t = parseString(memJsonString);
			return t;
		}
		
		//------------从本地读取------------------
		t=loadDataFromLocal(key);
		
		if (t!=null) {//本地有数据
			LogUtils.e("debug", "从本地加载数据，路径为："+getCacheFile(key).getAbsolutePath());
			return t;
		}
		
		//----------从网络加载数据----------------
		t = loadDataFromNet(index);
		
		if (t!=null) {
			LogUtils.e("debug", "从网络获取数据");
			return t;
		}
		return null;
			
	}
	
	/**
	 * 
	 * TODO<从本地读取数据>
	 * @param index
	 * @return
	 * @throw
	 * @return T
	 */
	private T loadDataFromLocal(String key) {
		File cacheFile = getCacheFile(key);
		//文件是否存在
		if (cacheFile.exists()) {
			//读取第一行，判断是否过期
			BufferedReader mReader=null;
			try {
				mReader = new BufferedReader(new FileReader(cacheFile));
				String insertTimeStr = mReader.readLine();
				long insertTime = Long.parseLong(insertTimeStr);
				
				if (System.currentTimeMillis()-insertTime>Constants.PROTOCOLTIME) {
					//读取缓存内容
					String lacJsonString = mReader.readLine();
					//存到内存
					MyApplication app=(MyApplication) UIUtils.getContext();
					app.getProtocolCacheMap().put(key, lacJsonString);
					
					//解析后返回
					return parseString(lacJsonString);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				IOUtils.close(mReader);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * TODO<得到缓存的文件>
	 * @param key
	 * @return
	 * @throw
	 * @return File
	 */
	private File getCacheFile(String key) {
		String dir=FileUtils.getDir("json");//sdcard/Android/data/包目录/json
		File cacheFile = new File(dir,key);
		return cacheFile;
	}
	
	/**
	 * 
	 * TODO<拼接将要缓存的协议关键字>
	 * @param index
	 * @return
	 * @throw
	 * @return String
	 */
	private String generateKey(int index) {
		return getInterfaceKey()+"."+index;
	}

	/**
	 * 
	 * TODO<从网络加载数据>
	 * @param index
	 * @return
	 * @throws IOException
	 * @throw
	 * @return T
	 */
	private T loadDataFromNet(int index) throws IOException {
		// http://localhost:8080/GooglePlayServer/home?index=0
		// 通过okHttp发起网络请求
		// 创建okHttpClient实例对象
		OkHttpClient okHttpClient = new OkHttpClient();

		// http://localhost:8080/GooglePlayServer/home?index=0
		String url = Constants.URLS.BASEURL + getInterfaceKey()+"?";

		// 参数
		Map<String, Object> parmasMap = new HashMap<String, Object>();
		parmasMap.put("index", "" + index);
		String urlParamsByMap = HttpUtils.getUrlParamsByMap(parmasMap);

		// 拼接之后的结果
		url = url + urlParamsByMap;

		// 创建一个请求对象
		Request request = new Request.Builder().get().url(url).build();

		// 发起请求
		Response response = okHttpClient.newCall(request).execute();

		if (response.isSuccessful()) {// 响应成功
			String resultJsonString = response.body().string();
			
			//存数据到内存
			LogUtils.e("debug", "写入缓存");
			MyApplication app=(MyApplication) UIUtils.getContext();
			String key=generateKey(index);
			app.getProtocolCacheMap().put(key, resultJsonString);
			
			//存数据到本地
			LogUtils.e("debug", "写入本地");
			File cacheFile = getCacheFile(key);
			BufferedWriter writer=null;
			try {
				writer = new BufferedWriter(new FileWriter(cacheFile));
				writer.write(System.currentTimeMillis()+"");
				writer.newLine();
				writer.write(resultJsonString);
			}finally{
				IOUtils.close(writer);
			}
			T t=parseString(resultJsonString);

			return t;
		}
		return null;
	}
	
	/**
	 * 
	 * TODO<解析网络请求返回的内容>
	 * @param resultJsonString
	 * @return
	 * @throw
	 * @return T
	 */
	public abstract T parseString(String resultJsonString);


	/**
	 * 
	 * TODO<交由子类实现，返回协议关键字>
	 * @return
	 * @throw
	 * @return String
	 */
	public abstract String getInterfaceKey();
}
