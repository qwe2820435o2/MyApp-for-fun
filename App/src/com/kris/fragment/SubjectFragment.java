package com.kris.fragment;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.kri.base.BaseHolder;
import com.kri.base.SuperBaseFragment;
import com.kri.bean.SubjectInfoBean;
import com.kri.holder.SubjectHolder;
import com.kri.protocol.SubjectProtocol;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SubjectFragment extends SuperBaseFragment {
	private SubjectProtocol mProtocol;
	private List<SubjectInfoBean> mData;

	@Override
	public LoadResultState initData() {
		try {
			mProtocol = new SubjectProtocol();
			mData = mProtocol.loadData(0);
			return checkResData(mData);
		} catch (IOException e) {
			e.printStackTrace();
			return LoadingPageController.LoadResultState.ERROR;
		}
	}

	@Override
	public View initSuccessView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setAdapter(new SubjectAdapter(listView, mData));
		return listView;
	}
	
	class SubjectAdapter extends SuperBaseAdapter<SubjectInfoBean>{

		public SubjectAdapter(AbsListView absListView, List datas) {
			super(absListView, datas);
		}


		@Override
		public BaseHolder getSpecialHolder(int position) {
			return new SubjectHolder();
		}
		
		@Override
		public boolean hasLoadMore() {
			return true;
		}

		@Override
		public List<SubjectInfoBean> initLoadMoreData() throws Exception {
			SystemClock.sleep(2000);
			List<SubjectInfoBean> loadData = mProtocol.loadData(mData.size());
			return loadData;
		}
	}
}
