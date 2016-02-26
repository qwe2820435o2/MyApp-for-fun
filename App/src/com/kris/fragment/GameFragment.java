package com.kris.fragment;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.kri.base.BaseHolder;
import com.kri.base.SuperBaseFragment;
import com.kri.bean.ItemInfoBean;
import com.kri.holder.ItemHolder;
import com.kri.protocol.GameProtocol;
import com.kri.utils.UIUtils;
import com.kris.adapter.SuperBaseAdapter;
import com.kris.widget.LoadingPageController;
import com.kris.widget.LoadingPageController.LoadResultState;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class GameFragment extends SuperBaseFragment {
	private GameProtocol mGameProtocol;
	private List<ItemInfoBean> mLoadData;

	@Override
	public LoadResultState initData() {
		mGameProtocol = new GameProtocol();
		try {
			mLoadData = mGameProtocol.loadData(0);
			return checkResData(mLoadData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LoadingPageController.LoadResultState.ERROR;
		}
	}

	@Override
	public View initSuccessView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setAdapter(new GameAdapter(listView,mLoadData));
		return listView;
	}
	
	class GameAdapter extends SuperBaseAdapter<ItemInfoBean>{

		public GameAdapter(AbsListView absListView, List datas) {
			super(absListView, datas);
		}

		@Override
		public BaseHolder getSpecialHolder() {
			return new ItemHolder();
		}
		
		@Override
		public boolean hasLoadMore() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public List<ItemInfoBean> initLoadMoreData() throws Exception {
			SystemClock.sleep(2000);
			List<ItemInfoBean> itemInfoBean = mGameProtocol.loadData(mLoadData.size());
			return itemInfoBean;
		}
	}
}
