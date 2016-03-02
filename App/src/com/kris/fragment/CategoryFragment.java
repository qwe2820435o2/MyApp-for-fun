package com.kris.fragment;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import com.kri.base.BaseHolder;
import com.kri.base.SuperBaseFragment;
import com.kri.bean.CategoryInfoBean;
import com.kri.holder.CategoryNormalHolder;
import com.kri.holder.CategoryTitleHolder;
import com.kri.protocol.CategoryProtocol;
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

public class CategoryFragment extends SuperBaseFragment {
	private List<CategoryInfoBean> mLoadData;

	@Override
	public LoadResultState initData() {
		try {
			CategoryProtocol categoryProtocol = new CategoryProtocol();
			mLoadData = categoryProtocol.loadData(0);
			return checkResData(mLoadData);
		} catch (IOException e) {
			e.printStackTrace();
			return LoadingPageController.LoadResultState.ERROR;
		}
	}

	@Override
	public View initSuccessView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setAdapter(new CategoryAdapter(listView, mLoadData));
		return listView;
	}
	
	class CategoryAdapter extends SuperBaseAdapter<CategoryInfoBean>{

		public CategoryAdapter(AbsListView absListView, List datas) {
			super(absListView, datas);
		}

		@Override
		public BaseHolder getSpecialHolder(int position) {
			CategoryInfoBean categoryInfoBean = mLoadData.get(position);
			
			if (categoryInfoBean.isTitle) {
				return new CategoryTitleHolder();
			}else {
				return new CategoryNormalHolder();
			}
		}
		
		@Override
		public int getViewTypeCount() {
			//有三种类型
			return 3;
		}
		
		@Override
		public int getNormalItemViewType(int position) {
			CategoryInfoBean categoryInfoBean = mLoadData.get(position);
			if (categoryInfoBean.isTitle) {
				return 2;
			}else {
				return 1;
			}
		}
	}
	
}
