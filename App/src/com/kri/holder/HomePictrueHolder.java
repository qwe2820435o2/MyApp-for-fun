package com.kri.holder;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kri.base.BaseHolder;
import com.kri.config.Constants;
import com.kri.utils.LogUtils;
import com.kri.utils.UIUtils;
import com.kris.app.R;
import com.kris.widget.ChildViewPager;
import com.squareup.picasso.Picasso;

public class HomePictrueHolder extends BaseHolder<List<String>> implements OnPageChangeListener {

	private LinearLayout mItemHomePicturecontainer;
	private ChildViewPager mItemHomePicturePager;
	String iv1="http://192.168.1.103:8080/GooglePlayServer/image?name=image/home01.jpg";
	String iv2="http://192.168.1.103:8080/GooglePlayServer/image?name=image/home02.jpg";
	String iv3="http://192.168.1.103:8080/GooglePlayServer/image?name=image/home03.jpg";
	String iv4="http://192.168.1.103:8080/GooglePlayServer/image?name=image/home04.jpg";
	String iv5="http://192.168.1.103:8080/GooglePlayServer/image?name=image/home05.jpg";
	String iv6="http://192.168.1.103:8080/GooglePlayServer/image?name=image/home06.jpg";
	String iv7="http://192.168.1.103:8080/GooglePlayServer/image?name=image/home07.jpg";
	String iv8="http://192.168.1.103:8080/GooglePlayServer/image?name=image/home08.jpg";
	
	String[] mData={iv1,iv2,iv3,iv4,iv5,iv6,iv7,iv8};

	@Override
	public View initHolderView() {
		View view = View.inflate(UIUtils.getContext(), R.layout.item_home_picture, null);
		mItemHomePicturecontainer = (LinearLayout) view.findViewById(R.id.item_home_picture_container_indicator);
		mItemHomePicturePager = (ChildViewPager) view.findViewById(R.id.item_home_picture_pager);
		return view;
	}

	@Override
	public void refreshHolderView(List<String> data) {
		//保存数据为成员变量
		mItemHomePicturePager.setAdapter(new HomePictureAdapter());
		
		for (int i = 0; i < mData.length; i++) {
			ImageView iv = new ImageView(UIUtils.getContext());
			iv.setImageResource(R.drawable.indicator_normal);
			if (i==0) {
				iv.setImageResource(R.drawable.indicator_selected);
			}
			int width=UIUtils.dip2Px(5);
			int height=UIUtils.dip2Px(5);
			
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(width, height);
			params.bottomMargin=UIUtils.dip2Px(5);
			params.leftMargin=UIUtils.dip2Px(5);
			
			mItemHomePicturecontainer.addView(iv, params);
		}
		
		mItemHomePicturePager.setOnPageChangeListener(this);
		
		//无限轮播
		int diff=Integer.MAX_VALUE/2%mData.length;
		int index=Integer.MAX_VALUE/2-diff;
		mItemHomePicturePager.setCurrentItem(index);
		
		//自动轮播
		final AutoScrollTask autoScrollTask = new AutoScrollTask();
		autoScrollTask.start();
		
		//按下去停止轮播,松开又开始轮播
		mItemHomePicturePager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					autoScrollTask.stop();
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					autoScrollTask.start();
					break;
				case MotionEvent.ACTION_CANCEL:
					
					break;

				default:
					break;
				}
				return false;
			}
		});
	}
	
	//自动轮播
	class AutoScrollTask implements Runnable{
		
		public void start(){
			UIUtils.getMainHandler().postDelayed(this, 3000);
		}
		
		public void stop(){
			UIUtils.getMainHandler().removeCallbacks(this);
		}
		
		@Override
		public void run() {
			int currentItem = mItemHomePicturePager.getCurrentItem();
			currentItem++;
			mItemHomePicturePager.setCurrentItem(currentItem);
			start();
		}
		
	}
	
	class HomePictureAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			if (mData!=null) {
				//为了做轮播图的无限轮播
				return Integer.MAX_VALUE;
			}
			
			return 0;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view==object;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//防止无限轮播时越界
			position=position%mData.length;
			
			ImageView iv = new ImageView(UIUtils.getContext());
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			//data
			//加载图片
			Picasso.with(UIUtils.getContext()).load(mData[position]).into(iv);
			container.addView(iv);
			return iv;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		//防止无限轮播时越界
		position=position%mData.length;
		
		for (int i = 0; i < mData.length; i++) {
			ImageView iv = (ImageView) mItemHomePicturecontainer.getChildAt(i);
			iv.setImageResource(R.drawable.indicator_normal);
			if (i==position) {
				iv.setImageResource(R.drawable.indicator_selected);
			}
		}
	}
}
