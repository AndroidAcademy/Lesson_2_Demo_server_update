package com.jon.demoserverupdate.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MainApplication
		extends Application {

	//==============================================
	//              Fields
	//==============================================

	private static Context mAppContext;
	private static Handler mUIhanlder;

	//==============================================
	//              Application Life cycle
	//==============================================

	@Override
	public void onCreate() {
		super.onCreate();

		mAppContext = getApplicationContext();
		mUIhanlder = new Handler();

	}

	//==============================================
	//              Getter Method
	//==============================================

	public static Context getAppContext() {
		return mAppContext;
	}

	public static Handler getUIhanlder() {
		return mUIhanlder;
	}
}
