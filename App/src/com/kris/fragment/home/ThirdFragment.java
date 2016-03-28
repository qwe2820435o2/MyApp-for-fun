package com.kris.fragment.home;

import java.lang.reflect.Field;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.kri.base.BaseFragment;
import com.kris.app.R;

public class ThirdFragment extends BaseFragment {


	@Override
	protected View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_third, null);
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
