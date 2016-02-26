package com.kri.protocol;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.kri.base.BaseProtocol;
import com.kri.bean.HomeInfoBean;
import com.kri.config.Constants;
import com.kri.utils.HttpUtils;
import com.kris.widget.LoadingPageController.LoadResultState;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/**
 * 
 * TODO<负责HomeFragment里面的网络请求>
 * 
 * @author LuLiLi
 * @data: 2016-2-21 上午12:09:33
 * @version: V1.0
 * @param <HomeInfoBean>
 * @param <pictrueInfoBean> index 分页请求的索引
 * @param <HomeInfoBean>
 */
public class HomeProtocol extends BaseProtocol<HomeInfoBean>{

	@Override
	public HomeInfoBean parseString(String resultJsonString) {
		Gson gson = new Gson();
		return gson.fromJson(resultJsonString, HomeInfoBean.class);
	}

	@Override
	public String getInterfaceKey() {
		return "home";
	}
	
}
