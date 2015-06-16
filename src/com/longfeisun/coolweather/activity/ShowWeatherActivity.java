package com.longfeisun.coolweather.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.longfeisun.coolweather.R;
import com.longfeisun.coolweather.util.CallBackListener;
import com.longfeisun.coolweather.util.HttpUtil;
import com.longfeisun.coolweather.util.LogUtil;
import com.longfeisun.coolweather.util.Utility;

public class ShowWeatherActivity extends BaseActivity implements
		View.OnClickListener {
	private static final String TAG = "ShowWeatherActivity";

	private ProgressDialog dialog;
	private TextView tv_title;
	private TextView tv_pushtime;
	private TextView tv_date;
	private TextView tv_weather;
	private TextView tv_temp1;
	private TextView tv_temp2;
	private ImageButton ib_refresh;
	private ImageButton ib_home;
	private boolean finishFlag = false;

	public static void actionStart(Context context, String countyCode) {
		Intent intent = new Intent(context, ShowWeatherActivity.class);
		intent.putExtra("countyCode", countyCode);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_pushtime = (TextView) this.findViewById(R.id.tv_pushtime);
		tv_date = (TextView) this.findViewById(R.id.tv_date);
		tv_weather = (TextView) this.findViewById(R.id.tv_weather);
		tv_temp1 = (TextView) this.findViewById(R.id.tv_temp1);
		tv_temp2 = (TextView) this.findViewById(R.id.tv_temp2);
		ib_refresh = (ImageButton) this.findViewById(R.id.ib_refresh);
		ib_home = (ImageButton) this.findViewById(R.id.ib_home);
		ib_refresh.setOnClickListener(this);
		ib_home.setOnClickListener(this);
		String countyCode = getIntent().getStringExtra("countyCode");
		dialog = new ProgressDialog(ShowWeatherActivity.this);
		dialog.setMessage("正在获取天气信息...");

		if (TextUtils.isEmpty(countyCode)) {
			
			
			if(isHasNet()){
				 SharedPreferences pref = PreferenceManager
				 .getDefaultSharedPreferences(ShowWeatherActivity.this);
				 String weatherCode = pref.getString("weatherCode", "");
				 queryWeatherInfo(weatherCode);
			} else {
				showWeather();
			}
			
		} else {
			dialog.show();
			queryWeatherCode(countyCode);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void queryFromRequest(final String code, final String flag) {
		String path = "";

		if ("weather".equals(flag)) {
			path = "http://www.weather.com.cn/adat/cityinfo/" + code + ".html";
		} else {
			path = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		}

		HttpUtil.sendHttpRequest(path, new CallBackListener() {
			@Override
			public void onFinished(String response) {
				if ("county".equals(flag)) {
					if (!TextUtils.isEmpty(code)) {
						String[] arr = response.split("\\|");
						queryWeatherInfo(arr[1]);
					}
				} else if ("weather".equals(flag)) {
					LogUtil.i(TAG, response);
					if (!TextUtils.isEmpty(response)) {
						Utility.handleWeatherResponse(ShowWeatherActivity.this,
								response);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								showWeather();
								dialog.dismiss();
							}
						});
					}
				}
			}

			@Override
			public void onError(Exception e) {
			}
		});
	}

	private void queryWeatherCode(String countyCode) {
		queryFromRequest(countyCode, "county");
	}

	private void queryWeatherInfo(String weatherCode) {
		queryFromRequest(weatherCode, "weather");
	}

	private void showWeather() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(ShowWeatherActivity.this);
		tv_title.setText(pref.getString("cityName", "空"));
		tv_pushtime.setText("今天" + pref.getString("ptime", "空") + "发布");
		tv_date.setText(pref.getString("currenttime", "空"));
		tv_weather.setText(pref.getString("weather", "空"));
		tv_temp1.setText(pref.getString("temp1", "空"));
		tv_temp2.setText(pref.getString("temp2", "空"));
	}

	@Override
	public void onClick(View v) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(ShowWeatherActivity.this);
		switch (v.getId()) {
		case R.id.ib_refresh:
			dialog.show();
			queryFromRequest(pref.getString("weatherCode", ""), "weather");
			break;
		case R.id.ib_home:
			ChooseAreaActivity.actionStart(ShowWeatherActivity.this);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (finishFlag) {
			ActivityCollector.finishAll();
		}
		Toast.makeText(ShowWeatherActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT)
				.show();
		finishFlag = true;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(isHasNet()){
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(ShowWeatherActivity.this);
			String weatherCode = pref.getString("weatherCode", "");
			queryWeatherInfo(weatherCode);
		}
	}

	private boolean isHasNet() {
		boolean flag = false;
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = manager.getActiveNetworkInfo();
		if(mNetworkInfo != null){
			flag = true;
		}
		return flag;
	}

}
