package com.kri.base;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Properties;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {
	// 上下文
	private Context mContext;
	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// 单例
	private static CrashHandler instance;

	// log tag
	private static final String TAG = "CrashHandler";
	// 是否开启日志标记
	private static final boolean DEBUG = false;
	// 使用property来保存设备的信息和错误的堆栈信息
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSIONNAME = "versionname";
	private static final String VERSIONCODE = "versioncode";
	private static final String STACK_TRACE = "STACK_TRACE";
	// 错误报告的扩展名
	private static final String CRASH_REPORTER_EXTENSION = ".cr";

	private CrashHandler() {

	}

	private static CrashHandler getInstance() {
		if (instance == null) {
			instance = new CrashHandler();
		}
		return instance;
	}

	/**
	 * 初始化context、系统默认的UncaughtException处理器、设置该CrashHandler为程序的默认处理器
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当发生uncaughtException时，转入此函数进行处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

	}

	/**
	 * 自定义错误处理，收集错误信息 发送错误报告信息都在此完成 开发者根据自己的需求来自定义处理逻辑
	 * 
	 * @param ex
	 * @return
	 */
	private boolean handlerException(Throwable ex) {
		if (ex == null) {
			Log.w(TAG, "handleException--ex==null");
			return true;
		}

		final String msg = ex.getMessage();
		if (msg == null) {
			return false;
		}

		// 使用吐司来显示异常消息
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast toast = Toast.makeText(mContext, "程序出错，即将推出：\r\n" + msg,
						1);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Looper.loop();
			};
		}.start();

		// 收集设备信息
		collectCrashDeviceInfo(mContext);
		// 保存错误报告文件
		saveCrashInfoToFile(ex);
		
		return true;
	}
	
	/**
	 * 保存错误信息到文件中
	 * @param ex
	 * @return 
	 */
	private String saveCrashInfoToFile(Throwable ex) {
		
		Writer info = new StringWriter();   
		PrintWriter printWriter = new PrintWriter(info);   
		ex.printStackTrace(printWriter);   
		Throwable cause = ex.getCause();   
		while (cause != null) {   
			cause.printStackTrace(printWriter);   
			cause = cause.getCause();   
		}   
		String result = info.toString();   
		printWriter.close();   
		mDeviceCrashInfo.put("EXEPTION", ex.getLocalizedMessage());  
		mDeviceCrashInfo.put(STACK_TRACE, result);   
		try {   
			//long timestamp = System.currentTimeMillis();   
			Time t = new Time("GMT+8");   
			t.setToNow(); // 取得系统时间  
			int date = t.year * 10000 + t.month * 100 + t.monthDay;  
			int time = t.hour * 10000 + t.minute * 100 + t.second;  
			String fileName = "crash-" + date + "-" + time + CRASH_REPORTER_EXTENSION;   
			FileOutputStream trace = mContext.openFileOutput(fileName,   
					Context.MODE_PRIVATE);   
			mDeviceCrashInfo.store(trace, "");   
			trace.flush();   
			trace.close();   
			return fileName;   
		} catch (Exception e) {   
			Log.e(TAG, "an error occured while writing report file...", e);   
		}   
		return null;   
	}
	
	
	/**
	 * 收集程序崩溃的错误报告信息
	 * 
	 * @param context
	 */
	private void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSIONCODE,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSIONCODE, pi.versionCode + "");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e(TAG, "Error while collect package info:" + e.getMessage());
		}

		// 使用反射来收集设备信息，在Build类中包含各种设备信息
		// 例如：系统版本号，设备生产商等帮助调试程序的有用信息
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), "" + field.get(null));
				if (DEBUG) {
					Log.d(TAG, field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e(TAG, "Error while collect crash info:" + e.getMessage());
			}
		}
	}

}
