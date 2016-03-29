package com.kris.fragment.home;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kri.base.BaseFragment;
import com.kri.bean.UserInfo;
import com.kri.utils.Config;
import com.kris.activity.MainActivity;
import com.kris.app.R;

public class FourFragment extends BaseFragment {
	View ll_1, ll_2, ll_3, ll_4, ll_5, ll_6, ll_7, ll_msg, ll_rw, ll_rebate,
			ll_record, ll_switch, ll_mygame, ll_myorder, ll_fankui, ll_own;
	private TextView mUserNmae;
	private ImageView bang_touxiang;
	private TextView tv_name;
	private TextView login;
	private ImageView get_score;

	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_CAMER = 2;
	public static final int CROP_PHOTO_CODE = 12;

	@Override
	protected View initView() {
		View view = View.inflate(getActivity(), R.layout.fragment_four, null);
		ll_1 = view.findViewById(R.id.ll_1);
		ll_2 = view.findViewById(R.id.ll_2);
		ll_3 = view.findViewById(R.id.ll_3);
		ll_6 = view.findViewById(R.id.ll_6);
		ll_7 = view.findViewById(R.id.ll_7);
		ll_rw = view.findViewById(R.id.ll_rw);
		ll_msg = view.findViewById(R.id.ll_msg);
		ll_rebate = view.findViewById(R.id.ll_rebate);
		// 我的记录
		ll_record = view.findViewById(R.id.ll_record);
		mUserNmae = (TextView) view.findViewById(R.id.tv_name);

		ll_switch = (LinearLayout) view.findViewById(R.id.ll_switch);
		bang_touxiang = (ImageView) view.findViewById(R.id.bang_touxiang);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		login = (TextView) view.findViewById(R.id.user_login);

		photoTempPath = Config.getSDCardPath(getActivity())
				+ "/userprofile.jpg";

		// 设置头像
		bang_touxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectimg();
			}
		});

		return view;
	}
	Intent intent;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 切换账号
		case R.id.ll_switch:
			
			break;
		}
	}

	private void selectimg() {
		final CharSequence[] items = { "拍照上传", "从相册选择" };
		Dialog dialog = new AlertDialog.Builder(getActivity())
				.setTitle("选择图片来源")
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == SELECT_PICTURE) {
							toGetLocalImage();
						} else {
							toGetCameraImage();
						}
					}
				}).create();
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// setBarPublishVisibility(View.GONE);
			}
		});
		dialog.show();
	}

	String photoTempPath = "";
	File out;

	/**
	 * 选择本地图片
	 */
	public void toGetLocalImage() {
		Intent intent = new Intent();
		out = new File(photoTempPath);
		if (!out.exists()) {
			try {
				out.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Uri uri = Uri.fromFile(out);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		// intent.setDataAndType(Intent.ACTION_GET_CONTENT, "image/*");
		startActivityForResult(intent, SELECT_PICTURE);
	}

	/**
	 * 照相选择图片
	 */
	public void toGetCameraImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 打开相机
		// String photoname = "userprofile.jpg";
		out = new File(photoTempPath);
		if (!out.exists()) {
			try {
				out.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Uri uri = Uri.fromFile(out);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(intent, SELECT_CAMER);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == android.app.Activity.RESULT_OK) {
			switch (requestCode) {
			case SELECT_PICTURE:
				if (null != data && null != data.getData())
					cropPhoto(data.getData());
				break;
			case SELECT_CAMER:
				if (out == null) {
					out = new File(Config.getSDCardPath(getActivity())
							+ "/userprofile.jpg");
				}
				cropPhoto(Uri.fromFile(out));

				break;

			case CROP_PHOTO_CODE:
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				bitmap = BitmapFactory.decodeFile(photoTempPath, options);
				bang_touxiang.setImageBitmap(bitmap);
				break;
			default:
				break;
			}
		}
	}

	Bitmap bitmap;
	public int crop = 300;// 裁剪大小

	/**
	 * 剪切
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("output", Uri.fromFile(out));
		intent.putExtra("crop", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", crop);
		intent.putExtra("outputY", crop);
		startActivityForResult(intent, CROP_PHOTO_CODE);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

}
