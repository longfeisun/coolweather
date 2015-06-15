package com.longfeisun.coolweather.test;

import android.test.AndroidTestCase;

import com.longfeisun.coolweather.db.CoolWeatherDB;
import com.longfeisun.coolweather.util.CallBackListener;
import com.longfeisun.coolweather.util.HttpUtil;
import com.longfeisun.coolweather.util.Utility;

public class HttpUtilTest extends AndroidTestCase {

	
	public void testSendHttpRequest(){
		HttpUtil.sendHttpRequest("http://www.weather.com.cn/data/list3/city.xml", new CallBackListener() {
			
			@Override
			public void onFinished(String response) {
				Utility.handleProvincesResponse(CoolWeatherDB.getInstance(getContext()), response);
			}
			
			@Override
			public void onError(Exception e) {
			}
		});
	}
	
}
