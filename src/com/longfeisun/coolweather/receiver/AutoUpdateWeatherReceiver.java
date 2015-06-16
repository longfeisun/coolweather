package com.longfeisun.coolweather.receiver;

import com.longfeisun.coolweather.service.AutoUpdateWeatherService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateWeatherReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		AutoUpdateWeatherService.actionStart(context);
	}

}
