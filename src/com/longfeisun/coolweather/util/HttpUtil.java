package com.longfeisun.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	private static final String TAG = "HttpUtil";
	public static void sendHttpRequest(final String path, final CallBackListener listener){
		
		LogUtil.i(TAG, "--path->>" + path);
		
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection conn = null;
				try {
					URL url = new URL(path);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(8000);
					conn.setReadTimeout(8000);
					conn.connect();
					if(conn.getResponseCode() == 200){
						InputStream in = conn.getInputStream();
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						String line = "";
						StringBuilder response = new StringBuilder();
						while((line = reader.readLine()) != null){
							response.append(line);
						}
						listener.onFinished(response.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.onError(e);
				} finally{
					if(conn!=null){
						conn.disconnect();
					}
				}
				
			}
		}).start();
	}
}
