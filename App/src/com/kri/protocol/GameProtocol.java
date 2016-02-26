package com.kri.protocol;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kri.base.BaseProtocol;
import com.kri.bean.ItemInfoBean;

public class GameProtocol extends BaseProtocol<List<ItemInfoBean>> {

	@Override
	public List<ItemInfoBean> parseString(String resultJsonString) {
		Gson gson = new Gson();
		return gson.fromJson(resultJsonString, new TypeToken<List<ItemInfoBean>>(){}.getType());
	}

	@Override
	public String getInterfaceKey() {
		return "game";
	}

}
