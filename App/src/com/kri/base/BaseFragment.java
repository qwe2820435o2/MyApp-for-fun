package com.kri.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract  class BaseFragment extends Fragment implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		init();
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return initView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		initData();
		initEvent();
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * 子类可以选择性地覆写该方法
	 * TODO<初始化>
	 * @throw
	 * @return void
	 */
	protected void init() {
		
	}
	
	/**
	 * 必须实现,基类不知道具体实现，定义成为抽象方法，交给子类具体实现
	 * TODO<初始化视图>
	 * @return
	 * @throw
	 * @return View
	 */
	protected abstract View initView();
	
	/**
	 * 子类可以选择性地覆写该方法
	 * TODO<初始化数据>
	 * @throw
	 * @return void
	 */
	protected void initData() {
		
	}
	
	/**
	 * 子类可以选择性地实现
	 * TODO<初始化监听>
	 * @throw
	 * @return void
	 */
	protected void initEvent() {
		
	}

}
