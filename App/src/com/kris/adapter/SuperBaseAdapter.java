package com.kris.adapter;

import java.util.List;

import com.kri.base.BaseHolder;
import com.kri.base.UIHandler;
import com.kri.factory.ThreadPoolProxyFactory;
import com.kri.holder.ItemHolder;
import com.kri.holder.LoadMoreHolder;
import com.kri.utils.UIUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public abstract class SuperBaseAdapter<ITEMBEANTYPE> extends MyBaseAdapter<ITEMBEANTYPE> implements OnItemClickListener{

	private static final int VIEWTYPE_NORMAL = 0;//普通条目类型
	private static final int VIEWTYPE_LOADMORE = 1;//加载更多条目类型
	private LoadMoreHolder mLoadMoreHolder;
	private LoadMoreTask mLoadMoreTask;
	private AbsListView mAbsListView;
	private int mLoadResultState;

	public SuperBaseAdapter(AbsListView absListView,List datas) {
		super(datas);
		mAbsListView=absListView;
		mAbsListView.setOnItemClickListener(this);
	}
	
	@Override
	public int getViewTypeCount() {
		//默认值为1，现为2.普通类型和加载更多的类型
		return super.getViewTypeCount()+1;
	}
	
	/**
	 * 注意：position范围：0~getViewTypeCount-1，也就是VIEWTYPE_NORMAL、VIEWTYPE_NORMAL要在这个范围内！
	 * 重载方法
	 * @param position
	 * @return
	 */
	@Override
	public int getItemViewType(int position) {
		if (position==getCount()-1) {
			return VIEWTYPE_LOADMORE;
		}else {
			return VIEWTYPE_NORMAL;
		}
	}
	
	/**
	 * 处理滑到底出现加载更多，需+1
	 * 重载方法
	 * @return
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return super.getCount()+1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//得到根布局
		BaseHolder holder=null;
		if (convertView==null) {
			//在这里根据Type进行判断决定根视图
			if (getItemViewType(position)==VIEWTYPE_NORMAL) {
				holder=getSpecialHolder();
			}else {
				holder=loadMoreHolder();
			}
		}else {
			holder=(BaseHolder) convertView.getTag();
		}
		
		//得到数据，绑定数据
		if (getItemViewType(position)==VIEWTYPE_NORMAL) {
			holder.setDataAndRefreshHolderView(mDataSet.get(position));
		}else {
			if (hasLoadMore()) {
				//滑到底部，触发加载更多数据
				triggerLoadMoreData();
			}else {
				holder.setDataAndRefreshHolderView(LoadMoreHolder.LOADMORE_NONE);
			}
		}
		
		return holder.mHolderView;
	}
	
	private LoadMoreHolder loadMoreHolder() {
		if (mLoadMoreHolder==null) {
			mLoadMoreHolder = new LoadMoreHolder();
		}
		return mLoadMoreHolder;
	}
	
	/**
	 * 
	 * TODO<决定是否有加载更多，默认没有加载更多；子类可以覆写该方法，修改默认行为>
	 * @return
	 * @throw
	 * @return boolean
	 */
	public  boolean hasLoadMore() {
		return false;
	}
	
	/**
	 * 
	 * TODO<触发加载更多数据>
	 * @throw
	 * @return void
	 */
	private void triggerLoadMoreData() {
		//防止多次请求数据
		if (mLoadMoreTask==null) {
			//处理loadMore的ui
			int state=LoadMoreHolder.LOADMORE_LOADING;
			mLoadMoreHolder.setDataAndRefreshHolderView(state);
			
			mLoadMoreTask = new LoadMoreTask();
			ThreadPoolProxyFactory.getmNormalThreadPoolProxy().execute(mLoadMoreTask);
		}
	}
	
	class LoadMoreTask implements Runnable{

		private static final int PAGESIZE = 20;//协议里每页请求的是20条数据
		

		@Override
		public void run() {
			//开始加载更多，得到数据
			List<ITEMBEANTYPE> loadMoreList=null;
			mLoadResultState = LoadMoreHolder.LOADMORE_LOADING;
			
			//处理数据
			try {
				loadMoreList=initLoadMoreData();
				
				if (loadMoreList==null) {
					mLoadResultState=LoadMoreHolder.LOADMORE_NONE;
				}else {
					if (loadMoreList.size()==PAGESIZE) {//回来的数据等于请求的分页条数
	;					mLoadResultState=LoadMoreHolder.LOADMORE_LOADING;
					}else {
						mLoadResultState=LoadMoreHolder.LOADMORE_NONE;
					}
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mLoadResultState=LoadMoreHolder.LOADMORE_ERROR;
			}
			
			//定义临时变量衔接，防止冲突
			final List<ITEMBEANTYPE> finalLoadMoreList=loadMoreList;
			final int finalState=mLoadResultState;
			
			//在主线程中刷新数据
			UIUtils.postTaskSafely(new Runnable() {
				
				@Override
				public void run() {
					if (finalLoadMoreList!=null) {
						mDataSet.addAll(finalLoadMoreList);
						notifyDataSetChanged();
						
					}
					loadMoreHolder().refreshHolderView(finalState);
				}
			});
			mLoadMoreTask=null;
		}
	}
	
	/**
	 * 
	 * TODO<在子线程中加载更多数据，滑到底部的时候触发,交由子类实现>
	 * @return
	 * @throw
	 * @return List<ITEMBEANTYPE>
	 */
	public List<ITEMBEANTYPE> initLoadMoreData() throws Exception{
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * TODO<返回一个BaseHolder的子类对象>
	 * @return
	 * @throw
	 * @return BaseHolder
	 */
	public abstract BaseHolder getSpecialHolder();
	
	/**
	 * 处理加载失败后的重新加载
	 * 重载方法
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (getItemViewType(position)==VIEWTYPE_LOADMORE) {
			if (mLoadResultState==LoadMoreHolder.LOADMORE_ERROR) {
				triggerLoadMoreData();
			}
		}else {
			//普通类型item的点击事件，交由子类实现
			onNormalItemClick(parent,view,position,id);
		}
	}
	
	/**
	 * 
	 * TODO<交由子类处理普通item的点击事件>
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 * @throw
	 * @return void
	 */
	public void onNormalItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}
}
