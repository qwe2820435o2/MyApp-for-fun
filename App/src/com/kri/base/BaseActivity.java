package com.kri.base;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.kri.base.UIHandler.IHandler;
import com.kri.utils.LogUtils;

public abstract class BaseActivity extends Activity implements OnClickListener {

	// 是否允许全屏
	private boolean mAllowFullScreen = true;

	public abstract void initView();

	public abstract void widgetClick(View v);

	protected static UIHandler mHandler = new UIHandler(Looper.getMainLooper());

	public void setAllowFullScreen(boolean allowFullScreen) {
		this.mAllowFullScreen = allowFullScreen;
	}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	// -----------------生命周期的监控---------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtils.d(this.getClass() + "------onCreate");
		// 竖屏锁定
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (mAllowFullScreen) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题
		}
		initView();
		setHandler();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtils.d(this.getClass() + "-----onStart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LogUtils.d(this.getClass() + "------onResume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LogUtils.d(this.getClass() + "------onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LogUtils.d(this.getClass() + "------onStop");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		LogUtils.d(this.getClass() + "-------onRestart");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LogUtils.d(this.getClass() + "------onDestroy");
	}

	private void setHandler() {
		mHandler.setHandler(new IHandler() {
			public void handleMessage(Message msg) {
				handler(msg);// 有消息就提交给子类实现的方法
			}
		});
	}

	// 让子类进行处理
	public void handler(Message msg) {

	}

	// 软键盘自动隐藏
	public void hiddeKey(EditText edText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edText.getWindowToken(), 0);
	}

	public void showKey(EditText edText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(edText, 0);
	}
	
	/**
	 * 检查是否存在SDCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
}
