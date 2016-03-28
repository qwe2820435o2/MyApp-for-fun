package com.kris.fragment.home;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kri.base.BaseFragment;
import com.kri.bean.DuitangInfo;
import com.kri.config.Constants;
import com.kri.utils.Helper;
import com.kri.utils.UIUtils;
import com.kris.app.R;
import com.kris.widget.XListView;
import com.kris.widget.XListView.IXListViewListener;
import com.squareup.picasso.Picasso;

public class SecondFragment extends BaseFragment implements IXListViewListener{
    private XListView mAdapterView = null;
    private StaggeredAdapter mAdapter = null;
    private int currentPage = 0;
    ContentTask task = new ContentTask(UIUtils.getContext(), 2);
    
	@Override
	public void onClick(View v) {
		
	}

	@Override
	protected View initView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.fragment_second, null);
		mAdapterView = (XListView) view.findViewById(R.id.list);
		mAdapterView.setPullLoadEnable(true);
		mAdapterView.setXListViewListener(this);
		
		mAdapter = new StaggeredAdapter(this.getActivity().getApplicationContext(), mAdapterView);
		
		return view;
	}

	private class ContentTask extends AsyncTask<String, Integer, List<DuitangInfo>> {

        private Context mContext;
        private int mType = 1;

        public ContentTask(Context context, int type) {
            super();
            mContext = context;
            mType = type;
        }

        @Override
        protected List<DuitangInfo> doInBackground(String... params) {
            try {
                return parseNewsJSON(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DuitangInfo> result) {
            if (mType == 1) {

                mAdapter.addItemTop(result);
                mAdapter.notifyDataSetChanged();
                mAdapterView.stopRefresh();

            } else if (mType == 2) {
                mAdapterView.stopLoadMore();
                mAdapter.addItemLast(result);
                mAdapter.notifyDataSetChanged();
            }

        }

        @Override
        protected void onPreExecute() {
        }

        public List<DuitangInfo> parseNewsJSON(String url) throws IOException {
            List<DuitangInfo> duitangs = new ArrayList<DuitangInfo>();
            String json = "";
            if (Helper.checkConnection(mContext)) {
                try {
                    json = Helper.getStringFromUrl(url);

                } catch (Exception e) {
                    Log.e("IOException is : ", e.toString());
                    e.printStackTrace();
                    return duitangs;
                }
            }
            Log.d("MainActiivty", "json:" + json);

            try {
                if (null != json) {
                    JSONObject newsObject = new JSONObject(json);
                    JSONObject jsonObject = newsObject.getJSONObject("data");
                    JSONArray blogsJson = jsonObject.getJSONArray("blogs");

                    for (int i = 0; i < blogsJson.length(); i++) {
                        JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
                        DuitangInfo newsInfo1 = new DuitangInfo();
                        newsInfo1.setAlbid(newsInfoLeftObject.isNull("albid") ? "" : newsInfoLeftObject.getString("albid"));
                        newsInfo1.setIsrc(newsInfoLeftObject.isNull("isrc") ? "" : newsInfoLeftObject.getString("isrc"));
                        newsInfo1.setMsg(newsInfoLeftObject.isNull("msg") ? "" : newsInfoLeftObject.getString("msg"));
                        newsInfo1.setHeight(newsInfoLeftObject.getInt("iht"));
                        duitangs.add(newsInfo1);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return duitangs;
        }
    }
	

	@Override
	public void onRefresh() {
		AddItemToContainer(++currentPage, 1);		
	}

	@Override
	public void onLoadMore() {
		 AddItemToContainer(++currentPage, 2);
	}
	
	/**
     * 添加内容
     * 
     * @param pageindex
     * @param type
     *            1为下拉刷新 2为加载更多
     */
    private void AddItemToContainer(int pageindex, int type) {
        if (task.getStatus() != Status.RUNNING) {
            String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
            Log.d("MainActivity", "current url:" + url);
            ContentTask task = new ContentTask(UIUtils.getContext(), type);
            task.execute(url);

        }
    }
	
	 public class StaggeredAdapter extends BaseAdapter {
	        private Context mContext;
	        private LinkedList<DuitangInfo> mInfos;
	        private XListView mListView;

	        public StaggeredAdapter(Context context, XListView xListView) {
	            mContext = context;
	            mInfos = new LinkedList<DuitangInfo>();
	            mListView = xListView;
	        }

			@Override
	        public View getView(int position, View convertView, ViewGroup parent) {

	            ViewHolder holder;
	            DuitangInfo duitangInfo = mInfos.get(position);

	            if (convertView == null) {
	                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
	                convertView = layoutInflator.inflate(R.layout.infos_list, null);
	                holder = new ViewHolder();
	                holder.imageView = (ImageView) convertView.findViewById(R.id.news_pic);
	                holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
	                convertView.setTag(holder);
	            }

	            holder = (ViewHolder) convertView.getTag();
	            holder.contentView.setText(duitangInfo.getMsg());
	            Picasso.with(UIUtils.getContext()).load(duitangInfo.getIsrc()).into(holder.imageView);
	            return convertView;
	        }

	        class ViewHolder {
	            ImageView imageView;
	            TextView contentView;
	            TextView timeView;
	        }

	        @Override
	        public int getCount() {
	            return mInfos.size();
	        }

	        @Override
	        public Object getItem(int arg0) {
	            return mInfos.get(arg0);
	        }

	        @Override
	        public long getItemId(int arg0) {
	            return 0;
	        }

	        public void addItemLast(List<DuitangInfo> datas) {
	            mInfos.addAll(datas);
	        }

	        public void addItemTop(List<DuitangInfo> datas) {
	            for (DuitangInfo info : datas) {
	                mInfos.addFirst(info);
	            }
	        }
	    }
	 
	@Override
	public void onResume() {
		super.onResume();
		 mAdapterView.setAdapter(mAdapter);
	     AddItemToContainer(currentPage, 2);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }
	
	@Override
	public void onDetach() {
	    super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
}
