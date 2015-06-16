package com.longfeisun.coolweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class BaseActivity extends Activity {
	
	
	private static final String TAG = "BaseActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, getClass().getSimpleName());
		ActivityCollector.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
	}
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
	
	
	
	
	
	
}
