package com.kris.fragment;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.kri.base.BaseHolder;
import com.kri.base.SuperBaseFragment;
import com.kri.bean.ItemInfoBean;
import com.kri.holder.ItemHolder;
import com.kri.protocol.AppProtocol;
import com.kri.utils.LogUtils;
import com.kri.utils.UIUtils;
import com.kris.adapter.SuperBaseAdapter;
import com.kris.widget.LoadingPageController;
import com.kris.widget.LoadingPageController.LoadResultState;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

public class AppFragment extends SuperBaseFragment {

	private List<ItemInfoBean> mData;
	private AppProtocol mAppProtocol;

	@Override
	public LoadResultState initData() {
		mAppProtocol = new AppProtocol();
		try {
			mData = mAppProtocol.loadData(0);
			return checkResData(mData);
		} catch (IOException e) {
			e.printStackTrace();
			return LoadingPageController.LoadResultState.ERROR;
		}
	}

	@Override
	public View initSuccessView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setAdapter(new AppAdapter(listView,mData));
		return listView;
	}
	
	class AppAdapter extends SuperBaseAdapter{

		public AppAdapter(AbsListView absListView, List datas) {
			super(absListView, datas);
		}

		@Override
		public BaseHolder getSpecialHolder(int position) {
			return new ItemHolder();
		}
		
		@Override
		public boolean hasLoadMore() {
			return true;
		}
		
		@Override
		public List initLoadMoreData() throws Exception {
			SystemClock.sleep(2000);
			List<ItemInfoBean> loadData = mAppProtocol.loadData(mData.size());
			return loadData;
			
		}
	}
}
