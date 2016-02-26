package com.kri.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kri.base.BaseProtocol;
import com.kri.bean.ItemInfoBean;

public class AppProtocol extends BaseProtocol<List<ItemInfoBean>> {

	@Override
	public List<ItemInfoBean> parseString(String resultJsonString) {
		Gson gson = new Gson();
		//jsonString-->list/map-->泛型解析
		return gson.fromJson(resultJsonString, new TypeToken<List<ItemInfoBean>>(){}.getType());
	}

	@Override
	public String getInterfaceKey() {
		return "app";
	}

}
