package com.kri.base;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.kri.utils.UIUtils;
import com.kris.widget.LoadingPageController;

public abstract class SuperBaseFragment extends Fragment {
	public LoadingPageController mController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (mController==null) {
			mController = new LoadingPageController(
					UIUtils.getContext()) {

				@Override
				public View initSuccessView() {
					// TODO Auto-generated method stub
					return SuperBaseFragment.this.initSuccessView();
				}

				@Override
				public LoadResultState initData() {
					// TODO Auto-generated method stub
					return SuperBaseFragment.this.initData();
				}
			};
		}else {
			//使用eclipse因为V4包版本的问题，要加上这段代码
			 ViewParent parent = mController.getParent();
			 if (parent!=null && parent instanceof ViewGroup) {
				((ViewGroup)parent).removeView(mController);
			}
		}
		
		mController.triggerLoadData();
		return mController;
	}
	
	/**
	 * 
	 * TODO<触发加载数据，外界需要加载数据的时候，调用这个方法>
	 * @return
	 * @throw
	 * @return LoadingPageController.LoadResultState
	 */
	public abstract LoadingPageController.LoadResultState initData();
	
	/**
	 * 
	 * TODO<初始化具体的成功视图>
	 * @return
	 * @throw
	 * @return View
	 */
	public abstract View initSuccessView();
	
	/**
	 * 
	 * TODO<根据返回回来的数据，返回具体的状态>
	 * @param resObj
	 * @return
	 * @throw
	 * @return LoadingPageController.LoadResultState
	 */
	public LoadingPageController.LoadResultState checkResData(Object resObj){
		if (resObj==null) {
			return LoadingPageController.LoadResultState.EMPTY;
		}
		
		if (resObj instanceof List) {
			if (((List) resObj).size()==0) {
				return LoadingPageController.LoadResultState.EMPTY;
			}
		}
		
		if (resObj instanceof Map) {
			if (((Map) resObj).size()==0) {
				return LoadingPageController.LoadResultState.EMPTY;
			}
		}
		
		return LoadingPageController.LoadResultState.SUCCESS;
	}
}
