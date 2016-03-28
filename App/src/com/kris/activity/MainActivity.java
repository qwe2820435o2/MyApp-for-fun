package com.kris.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.kri.base.BaseFragment;
import com.kri.bean.Account;
import com.kri.bean.UserInfo;
import com.kri.utils.LogUtils;
import com.kri.utils.SPUtils;
import com.kris.app.R;
import com.kris.fragment.home.FirstFragment;
import com.kris.fragment.home.FourFragment;
import com.kris.fragment.home.SecondFragment;
import com.kris.fragment.home.ThirdFragment;
import com.kris.widget.WeiuuDialog;
import com.kris.widget.WeiuuDialog.OnDialogListener;
import com.u8.sdk.IU8SDKListener;
import com.u8.sdk.InitResult;
import com.u8.sdk.PayResult;
import com.u8.sdk.U8Code;
import com.u8.sdk.U8SDK;
import com.u8.sdk.plugin.U8User;
import com.u8.sdk.verify.UToken;



public class MainActivity extends FragmentActivity implements OnClickListener{
	private ProgressDialog loadingActivity;
	private WeiuuDialog mDialog;
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
		
		U8SDK.getInstance().setSDKListener(new IU8SDKListener() {
			@Override
			public void onResult(final int code, final String message) {

				U8SDK.getInstance().runOnMainThread(new Runnable() {

					@Override
					public void run() {
						Log.d("U8SDK", "onResult:" + message);
						switch (code) {
						case U8Code.CODE_INIT_SUCCESS:
							LogUtils.e("U8SDK", "初始化成功");
							break;
						case U8Code.CODE_INIT_FAIL:
							LogUtils.e("U8SDK", "初始化失败");
							break;
						case U8Code.CODE_LOGIN_FAIL:
							LogUtils.e("U8SDK", "登录失败");
							break;
						case U8Code.CODE_SHARE_SUCCESS:
							LogUtils.e("U8SDK", "分享成功");
							break;
						case U8Code.CODE_SHARE_FAILED:
							LogUtils.e("U8SDK", "分享失败");
							break;
						case U8Code.CODE_LOGOUT_SUCCESS:
							
							break;
						case U8Code.CODE_EXIT_SUCCESS:
							
							break;
						case U8Code.CODE_EXIT_CANCEL:
							
							break;
						default:
							LogUtils.e("U8SDK", "message");
						}
					}
				});

			}

			@Override
			public void onLoginResult(String result) {
				MainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						LogUtils.e("U8SDK", "SDK 登录成功,准备走登录 ");
						
					}
				});

			}

			@Override
			public void onAuthResult(final UToken authResult) {
				MainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (authResult.isSuc()) {
							LogUtils.e("U8SDK", "获取Token成功:"+authResult.getToken());
							LoginGameTask task = new LoginGameTask();
							task.execute(authResult);
							
						} else {
							LogUtils.e("U8SDK", "获取Token失败");
						}

					}
				});
			}

			@Override
			public void onSwitchAccount() {
				
			}

			@Override
			public void onSwitchAccount(String result) {
				
			}

			@Override
			public void onLogout() {
				
			}

			@Override
			public void onPayResult(final PayResult result) {
				
			}

			@Override
			public void onInitResult(InitResult result) {
				
			}

		});
		
		U8SDK.getInstance().init(MainActivity.this);

		U8SDK.getInstance().onCreate();
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
		mId = v.getId();
		if (navigateMap.indexOfKey(mId)>=0) {
			
			if (!v.isSelected()) {
				hideorshow(getSupportFragmentManager(), mId);
			}else {
				LogUtils.d(TAG, "ignore---selected!!!");
			}
		}
	}
	
	public void login() {
		U8SDK.getInstance().runOnMainThread(new Runnable() {

			@Override
			public void run() {
				// 请放到UI线程中执行，不然可能调不出登录界面
				U8User.getInstance().login();
			}
		});
	}
	
	private UserInfo mUserInfo;
	
	class LoginGameTask extends AsyncTask<UToken, Void, Void> {
		private UToken sdkInfo;
		
		protected void onPreExecute() {
			showProgressDialog(MainActivity.this, "正在登录,请稍候...");
		}

		@Override
		protected Void doInBackground(UToken... arg) {
			sdkInfo=arg[0];
			mUserInfo = new UserInfo(MainActivity.this);
			mUserInfo.setUserId(sdkInfo.getUserID()+"");
			mUserInfo.setUserName(sdkInfo.getUsername());
			return null;
		}

		protected void onPostExecute(Void account) {
			hideProgressDialog(MainActivity.this);
			onLoginGameSuccess();
		}

	}
	
	private void showProgressDialog(Activity context, String title) {
		if (loadingActivity != null) {
			return;
		}

		loadingActivity = new ProgressDialog(context);
		loadingActivity.setIndeterminate(true);
		loadingActivity.setCancelable(true);
		loadingActivity.setMessage(title);
		loadingActivity
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface arg0) {
					}
				});

		loadingActivity.show();
	}
	
	private void hideProgressDialog(Activity context) {
		if (loadingActivity == null) {
			return;
		}
		loadingActivity.dismiss();
		loadingActivity = null;
	}
	
	public void onLoginGameSuccess() {
		//存用户id、用户名到SP后开启Fragment
		LogUtils.e("U8SDK", "已存值到SP中，准备开启第四个fragment");
		hideorshow(getSupportFragmentManager(), mId);
	}
	
	public UserInfo getUser(){
		return mUserInfo;
	}
	
	private long mExitTime;
	private int mId;
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		U8SDK.getInstance().onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void onStart() {
		U8SDK.getInstance().onStart();
		super.onStart();
	}

	public void onPause() {
		U8SDK.getInstance().onPause();
		super.onPause();
	}

	public void onResume() {
		U8SDK.getInstance().onResume();
		super.onResume();
	}

	public void onNewIntent(Intent newIntent) {
		U8SDK.getInstance().onNewIntent(newIntent);
		super.onNewIntent(newIntent);
	}

	public void onStop() {
		U8SDK.getInstance().onStop();
		super.onStop();
	}

	public void onDestroy() {
		U8SDK.getInstance().onDestroy();
		super.onDestroy();
	}

	public void onRestart() {
		U8SDK.getInstance().onRestart();
		super.onRestart();
	}
}
