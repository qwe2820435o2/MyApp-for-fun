package com.kris.fragment.home;

import android.view.View;
import android.widget.TextView;

import com.kri.base.BaseFragment;
import com.kris.app.R;

public class SecondFragment extends BaseFragment {


	@Override
	protected View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_second, null);
		return view;
	}

	@Override
	public void onClick(View v) {
		
	}

}
