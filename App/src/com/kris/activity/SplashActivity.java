package com.kris.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.kri.base.BaseActivity;
import com.kri.config.Constants;
import com.kri.utils.SPUtils;
import com.kri.utils.V;
import com.kris.app.R;
import com.kris.widget.anim.Rotate3DAnimation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashActivity extends Activity {
	
	Class<?> activityClass;
	Class[] paramTypes = { Integer.TYPE, Integer.TYPE };

	Method overrideAnimation = null;
	private ImageView mIv_show;
	private boolean mIsFinish;
	private ImageView mIv_splash;
	private AnimationSet mAs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initAnimation();
	}

	private void initAnimation() {
		try {
			activityClass = Class.forName("android.app.Activity");
			overrideAnimation = activityClass.getDeclaredMethod(
					"overridePendingTransition", paramTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mIsFinish = SPUtils.getBoolean(getApplicationContext(),
						Constants.ISFINISH, false);
				
				Intent intent = new Intent();

				if (mIsFinish) {
					intent.setClass(SplashActivity.this, MainActivity.class);
				} else {
					intent.setClass(SplashActivity.this, GuideActivity.class);
				}
				startActivity(intent);
				finish();
				
				if (overrideAnimation != null) {
					try {
						overrideAnimation.invoke(SplashActivity.this, android.R.anim.fade_in,
								android.R.anim.fade_out);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, 2000);
	}

	public void initView() {
		setContentView(R.layout.activity_splash);
		mIv_splash = V.f(this, R.id.iv_splash);
	}

}
