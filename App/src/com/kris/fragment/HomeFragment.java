package com.kris.fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.os.SystemClock;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kri.base.BaseHolder;
import com.kri.base.SuperBaseFragment;
import com.kri.bean.ItemInfoBean;
import com.kri.bean.HomeInfoBean;
import com.kri.config.Constants;
import com.kri.holder.HomePictrueHolder;
import com.kri.holder.ItemHolder;
import com.kri.protocol.HomeProtocol;
import com.kri.utils.HttpUtils;
import com.kri.utils.LogUtils;
import com.kri.utils.OKHttpUtils;
import com.kri.utils.UIUtils;
import com.kris.adapter.SuperBaseAdapter;
import com.kris.widget.LoadingPageController;
import com.kris.widget.LoadingPageController.LoadResultState;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public class HomeFragment extends SuperBaseFragment {

	private List<String> mPictrue;
	private List<ItemInfoBean> mItemInfoBean;
	private HomeProtocol mPictureProtocol;

	@Override
	public LoadResultState initData() {
		try {
			mPictureProtocol = new HomeProtocol();
			HomeInfoBean pictrueInfoBean = mPictureProtocol.loadData(0);
			LoadResultState state = checkResData(pictrueInfoBean);
			
			if (state!=LoadingPageController.LoadResultState.SUCCESS) {//请求不成功，对象为空
				return state;
			}
			
			state=checkResData(pictrueInfoBean.list);
			
			if (state!=LoadingPageController.LoadResultState.SUCCESS) {//不成功，对象的list的size为空
				return state;
			}
			
			//保存到成员变量
			mPictrue=pictrueInfoBean.pictrue;
			mItemInfoBean=pictrueInfoBean.list;
			
			return state;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.e("debug", "错误信息为：" + e.getMessage());
			return LoadResultState.ERROR;
		}

	}

	@Override
	public View initSuccessView() {
		
		 ListView listView = new ListView(UIUtils.getContext());
		 HomePictrueHolder homePictrueHolder = new HomePictrueHolder();
		 listView.addHeaderView(homePictrueHolder.mHolderView);
		 //传数据给homePictrueHolder，让其刷新
		 homePictrueHolder.setDataAndRefreshHolderView(mPictrue);
		 
		 listView.setAdapter(new HomeAdapter(listView,mItemInfoBean));
		 
		 return listView;
		
		
	}
	
	

	class HomeAdapter extends SuperBaseAdapter<ItemInfoBean> {

		public HomeAdapter(AbsListView absListView,List datas) {
			super(absListView,datas);
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
			HomeInfoBean loadData = mPictureProtocol.loadData(mDataSet.size());
			if (loadData!=null) {
				return loadData.list;
			}
			return null;
		}
		
		@Override
		public void onNormalItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			LogUtils.e("debug", "进入item点击事件");
			Toast.makeText(UIUtils.getContext(), mDataSet.get(position).name, 1).show();
			super.onNormalItemClick(parent, view, position, id);
		}
	}
}
