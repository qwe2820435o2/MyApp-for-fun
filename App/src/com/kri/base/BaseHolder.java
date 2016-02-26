package com.kri.base;

import android.view.View;

/**
 * 
 * TODO<面向Holder编程，所有Holder的基类>
 * @author  LuLiLi
 * @data:  2016-2-5 下午5:09:10
 * @version:  V1.0
 */
public abstract class BaseHolder<HOLDERBEANTYPE> {
	public View mHolderView;//提供的视图
	
	private HOLDERBEANTYPE mData;
	
	public BaseHolder(){
		mHolderView=initHolderView();
		
		//convertView去找一个类的一个对象作为holder
		mHolderView.setTag(this);
	}
	
	/**
	 * 
	 * TODO<接收数据，绑定数据>
	 * @param data
	 * @throw
	 * @return void
	 */
	public void setDataAndRefreshHolderView(HOLDERBEANTYPE data){
		//保存数据到成员变量
		mData=data;
		refreshHolderView(data);
	}
	


	/**
	 * 
	 * TODO<持有的根视图，在子类初始化的时候调用>
	 * @return
	 * @throw
	 * @return View
	 */
	public abstract View initHolderView();
	
	/**
	 * 
	 * TODO<绑定数据>
	 * @param data
	 * @throw
	 * @return void
	 */
	public abstract void refreshHolderView(HOLDERBEANTYPE data);
}
