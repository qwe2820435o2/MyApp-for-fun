package com.kri.config;

import com.kri.utils.LogUtils;

/**
 * 
 * TODO<常量类，用来存放一些重要的常量信息>
 * @author  LuLiLi
 * @data:  2016-1-10 下午3:16:52
 * @version:  V1.0
 */
public interface Constants {
	
	/**
	 * LEVEL_ALL：显示所有日志
	 * LEVEL_OFF：关闭所有日志
	 */
	int DEBUGLEVEL=LogUtils.LEVEL_ALL;
	
	String ISFINISH="isfinish";
	String SPFILENAME="spfilename";
	long PROTOCOLTIME=5*60*1000;//5分钟
	
	public static final class URLS{
		public static final String BASEURL="http://192.168.1.103:8080/GooglePlayServer/";
		public static final String IMGBASEURL=BASEURL+"image?name=";
	
	}
}
