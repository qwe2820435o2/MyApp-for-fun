package com.kris.adapter;

import java.util.ArrayList;
import java.util.List;

import com.kri.utils.UIUtils;
import com.kris.app.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyBaseAdapter<ITEMBEANTYPE> extends BaseAdapter {
	public List<ITEMBEANTYPE> mDataSet=new ArrayList<ITEMBEANTYPE>();
	
	
	public MyBaseAdapter(List<ITEMBEANTYPE> datas) {
		super();
		mDataSet = datas;
	}

	@Override
	public int getCount() {
		if (mDataSet!=null) {
			return mDataSet.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDataSet!=null) {
			return mDataSet.get(position) ;
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
 		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		return null;
	}
	
}


