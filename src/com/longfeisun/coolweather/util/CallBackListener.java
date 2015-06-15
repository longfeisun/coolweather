package com.longfeisun.coolweather.util;

public interface CallBackListener {
	void onFinished(String response);

	void onError(Exception e);
}
