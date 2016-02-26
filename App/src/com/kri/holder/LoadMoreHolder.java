package com.kri.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.kri.base.BaseHolder;
import com.kri.utils.UIUtils;
import com.kris.app.R;
/**
 * 
 * TODO<加载更多情况的holder，有固定的视图和数据，所以我们只需要传Int进行判断>
 * @author  LuLiLi
 * @data:  2016-2-21 上午10:38:34
 * @version:  V1.0
 */
public class LoadMoreHolder extends BaseHolder<Integer>{
	
	public static final int LOADMORE_LOADING=0;//正在加载更多
	public static final int LOADMORE_ERROR=1;//加载更多失败
	public static final int LOADMORE_NONE=2;//没有加载更多
	

	private LinearLayout mLoading;
	private LinearLayout mError;

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_load_more, null);
		//找到子孩子
		
		mLoading = (LinearLayout) view.findViewById(R.id.item_loadmore_container_loading);
		mError = (LinearLayout) view.findViewById(R.id.item_loadmore_container_retry);
		return view;
	}

	@Override
	public void refreshHolderView(Integer state) {
		//隐藏视图
		mLoading.setVisibility(View.GONE);
		mError.setVisibility(View.GONE);
		
		//根据状态值判断显示
		switch (state) {
		case LOADMORE_LOADING:
			mLoading.setVisibility(View.VISIBLE);
			break;
		case LOADMORE_ERROR:
			mError.setVisibility(View.VISIBLE);
			break;
		case LOADMORE_NONE:
			//之前隐藏了，这里不做处理
			break;

		default:
			break;
		}
	}

}
