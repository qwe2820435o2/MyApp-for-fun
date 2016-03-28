package com.kris.fragment.home;

import java.lang.reflect.Field;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.kri.base.BaseFragment;
import com.kri.bean.UserInfo;
import com.kris.activity.MainActivity;
import com.kris.app.R;

public class FourFragment extends BaseFragment {
	View ll_1, ll_2, ll_3, ll_4, ll_5, ll_6, ll_7, ll_msg, ll_rw, ll_rebate,
	ll_record, ll_sign, ll_mygame,ll_myorder,ll_fankui,ll_own;
	private TextView mUserNmae;

	@Override
	protected View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_four, null);
		ll_1 = view.findViewById(R.id.ll_1);
		ll_2 = view.findViewById(R.id.ll_2);
		ll_3 = view.findViewById(R.id.ll_3);
		ll_4 = view.findViewById(R.id.ll_4);
		ll_5 = view.findViewById(R.id.ll_5);
		ll_6 = view.findViewById(R.id.ll_6);
		ll_7 = view.findViewById(R.id.ll_7);
		ll_rw = view.findViewById(R.id.ll_rw);
		ll_msg = view.findViewById(R.id.ll_msg);
		ll_rebate = view.findViewById(R.id.ll_rebate);
		// 我的记录
		ll_record = view.findViewById(R.id.ll_record);
		ll_own=view.findViewById(R.id.ll_own);
		mUserNmae = (TextView) view.findViewById(R.id.tv_name);
		return view;
	}
	
	@Override
	public void onClick(View v) {
		
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
