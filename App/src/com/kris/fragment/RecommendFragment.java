package com.kris.fragment;

import java.util.Random;

import com.kri.base.SuperBaseFragment;
import com.kri.utils.UIUtils;
import com.kris.widget.LoadingPageController;
import com.kris.widget.LoadingPageController.LoadResultState;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecommendFragment extends SuperBaseFragment {
	@Override
	public LoadResultState initData() {
		SystemClock.sleep(2000);
		
		LoadingPageController.LoadResultState[] loadedResultStateArr={LoadingPageController.LoadResultState.EMPTY,LoadingPageController.LoadResultState.ERROR,LoadingPageController.LoadResultState.SUCCESS};
		Random random = new Random();
		int index = random.nextInt(3);
		return loadedResultStateArr[index];
	}

	@Override
	public View initSuccessView() {
		TextView tv = new TextView(UIUtils.getContext());
		tv.setGravity(Gravity.CENTER);
		tv.setText(this.getClass().getSimpleName()+"的成功视图");
		return tv;
	}
}
