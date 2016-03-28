package com.kris.fragment.home;

import java.lang.reflect.Field;

import android.R.fraction;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.PagerSlidingTabStripExtends;
import com.google.gson.Gson;
import com.kri.base.BaseFragment;
import com.kri.base.SuperBaseFragment;
import com.kri.config.Constants;
import com.kri.factory.FragmentFractory;
import com.kri.utils.UIUtils;
import com.kri.utils.V;
import com.kris.app.R;
import com.kris.widget.LoadingPageController;

public class FirstFragment extends BaseFragment {

	private PagerSlidingTabStripExtends mTabs;
	private ViewPager mViewpager;
	private String[] mTitles;

	@Override
	protected View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_first, null);
		mTabs = V.f(view, R.id.first_tabs);
		mViewpager = V.f(view, R.id.first_viewPager);
		return view;
	}
	
	@Override
	protected void initData() {
		//初始化数据源
		mTitles = UIUtils.getStringArr(R.array.titles);
		//设置适配器
		mViewpager.setAdapter(new FirstFragmentPagerAdapter(getChildFragmentManager()));
	
		mTabs.setViewPager(mViewpager);
		
		//设置监听，选择页面时才加载
		final MyOnPageChangeListener listener = new MyOnPageChangeListener();
		mTabs.setOnPageChangeListener(listener);
		
		//手动选中触发第一页，但调用太早了，LoadingPager还没有渲染成功
		mViewpager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				listener.onPageSelected(0);
				mViewpager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
		
	}
	
	
	//解决：java.lang.IllegalStateException: No activity的问题，因为fragment中嵌套fragment会丢失
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
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
	private class FirstFragmentPagerAdapter extends FragmentPagerAdapter{

		public FirstFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = FragmentFractory.creatFragment(position);
			return fragment;
		}

		@Override
		public int getCount() {
			if (mTitles!=null) {
				return mTitles.length;
			}
			return 0;
		}
		
		//此方法需要覆写，不然源码里默认返回值为null
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return mTitles[position];
		}
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
		@Override
		public void onPageSelected(int position) {
			SuperBaseFragment selectFragment = FragmentFractory.creatFragment(position);
			LoadingPageController controller = selectFragment.mController;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
	}
	
}
