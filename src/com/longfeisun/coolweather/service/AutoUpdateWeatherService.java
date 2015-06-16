package com.longfeisun.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.longfeisun.coolweather.receiver.AutoUpdateWeatherReceiver;
import com.longfeisun.coolweather.util.CallBackListener;
import com.longfeisun.coolweather.util.HttpUtil;
import com.longfeisun.coolweather.util.Utility;

public class AutoUpdateWeatherService extends Service {

	private static final String TAG = "AutoUpdateWeatherService";
	
	public static void actionStart(Context context){
		Intent intent = new Intent(context, AutoUpdateWeatherService.class);
		context.startService(intent);
	}
	
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				 updateWeather();
			}
		}).start();

		
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 1*60*60*1000;
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AutoUpdateWeatherReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(AutoUpdateWeatherService.this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME, triggerAtTime, pi);
		
		return super.onStartCommand(intent, flags, startId);
	}

	private void updateWeather() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);

		String weatherCode = pref.getString("weatherCode", "");

		String path = "http://www.weather.com.cn/data/cityinfo/" + weatherCode
				+ ".html";

		HttpUtil.sendHttpRequest(path, new CallBackListener() {

			@Override
			public void onFinished(String response) {
				Utility.handleWeatherResponse(AutoUpdateWeatherService.this,
						response);
			}

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		});

	}

}
