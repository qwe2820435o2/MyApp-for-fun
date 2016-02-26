package com.kris.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.kri.base.BaseFragment;
import com.kri.utils.LogUtils;
import com.kris.app.R;
import com.kris.fragment.home.FirstFragment;
import com.kris.fragment.home.FourFragment;
import com.kris.fragment.home.SecondFragment;
import com.kris.fragment.home.ThirdFragment;



public class MainActivity extends FragmentActivity implements OnClickListener{
	
	private static final String TAG="MainActivity";
	//未获取更好的性能，用稀疏数组
	private SparseArray<BaseFragment> navigateMap=new SparseArray<BaseFragment>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//禁止横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//设置布局
		setContentView(R.layout.activity_main);
		//添加选项卡
		initFragment();
	}

	/**
	 * 
	 * TODO<初始化选项卡>
	 * @throw
	 * @return void
	 */
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		//先清空再添加
		navigateMap.clear();
		//初始化map
		mapNaviToFragment(R.id.rb_first,new FirstFragment());
		mapNaviToFragment(R.id.rb_second,new SecondFragment());
		mapNaviToFragment(R.id.rb_third,new ThirdFragment());
		mapNaviToFragment(R.id.rb_four,new FourFragment());
		
		//第一页默认显示
		hideorshow(fm,R.id.rb_first);
	}
	
	/**
	 * 
	 * TODO<显示和隐藏fragment>
	 * @param fm
	 * @param rbFirst
	 * @throw
	 * @return void
	 */
	private void hideorshow(FragmentManager fm, int id) {
		String tag = String.valueOf(id);
		FragmentTransaction transaction = fm.beginTransaction();
		if (null==fm.findFragmentByTag(tag)) {
			transaction.replace(R.id.main_content, navigateMap.get(id),tag);
		}else {
			transaction.show(navigateMap.get(id));
		}
		transaction.commit();
		//重置导航选中状态
		for (int i = 0; i < navigateMap.size(); i++) {
			int curId=navigateMap.keyAt(i);
			LogUtils.d(TAG, "curId:"+curId);
			if (curId==id) {
				findViewById(id).setSelected(true);
			}else {
				findViewById(curId).setSelected(false);
			}
		}
	}

	/**
	 * 
	 * TODO<初始化map>
	 * @param rbFirst
	 * @param fragment
	 * @throw
	 * @return void
	 */
	private void mapNaviToFragment(int id, BaseFragment fragment) {
		
		View view = findViewById(id);
		
		view.setOnClickListener(this);
		view.setSelected(false);
		navigateMap.put(id, fragment);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (navigateMap.indexOfKey(id)>=0) {
			if (!v.isSelected()) {
				hideorshow(getSupportFragmentManager(), id);
			}else {
				LogUtils.d(TAG, "ignore---selected!!!");
			}
		}
	}
	
	private long mExitTime;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {

				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
