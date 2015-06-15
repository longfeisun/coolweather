package com.longfeisun.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.R.color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.longfeisun.coolweather.R;
import com.longfeisun.coolweather.db.CoolWeatherDB;
import com.longfeisun.coolweather.model.City;
import com.longfeisun.coolweather.model.County;
import com.longfeisun.coolweather.model.Province;
import com.longfeisun.coolweather.util.CallBackListener;
import com.longfeisun.coolweather.util.HttpUtil;
import com.longfeisun.coolweather.util.LogUtil;
import com.longfeisun.coolweather.util.Utility;

public class ChooseAreaActivity extends BaseActivity {

	private static final String TAG = "ChooseAreaActivity";

	private static final int LEVEL_PROVINCE = 0;
	private static final int LEVEL_CITY = 1;
	private static final int LEVEL_COUNTY = 2;
	private int LEVEL_CURRENT = 3;

	private TextView tv_title;
	private ListView lv_area;
	private List<String> listData = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB db;

	private Province selectedProvince;
	private City selectedCity;
	private County selectedCounty;

	private List<Province> listProvinces;
	private List<City> listCites;
	private List<County> listCounties;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);
		db = CoolWeatherDB.getInstance(this);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
		lv_area = (ListView) this.findViewById(R.id.lv_area);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listData);
		lv_area.setAdapter(adapter);
		lv_area.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (LEVEL_CURRENT == LEVEL_PROVINCE) {
					selectedProvince = listProvinces.get(position);
					loadCity();
				} else if (LEVEL_CURRENT == LEVEL_CITY) {
					selectedCity = listCites.get(position);
					loadCounty();
				} else if (LEVEL_CURRENT == LEVEL_COUNTY) {
					selectedCounty = listCounties.get(position);
				}
			}
		});

		loadProvince();

	}

	public void loadProvince() {
		listProvinces = db.getAllProvinces();

		if (listProvinces != null && listProvinces.size() > 0) {
			listData.clear();
			for (Province p : listProvinces) {
				listData.add(p.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			lv_area.setSelection(0);
			tv_title.setText("全国");
			LEVEL_CURRENT = LEVEL_PROVINCE;

		} else {
			queryFromRequest("province", null);
		}
	}

	public void loadCity() {
		listCites = db.getCitiesbyProvinceCode(selectedProvince
				.getProvinceCode());

		if (listCites != null && listCites.size() > 0) {
			listData.clear();
			for (City c : listCites) {
				listData.add(c.getCityName());
			}
			adapter.notifyDataSetChanged();
			lv_area.setSelection(0);
			tv_title.setText(selectedProvince.getProvinceName());
			LEVEL_CURRENT = LEVEL_CITY;
		} else {
			queryFromRequest("city", selectedProvince.getProvinceCode());
		}
	}

	public void loadCounty() {
		listCounties = db.getCountiesByCityCode(selectedCity.getCityCode());
		if (listCounties != null && listCounties.size() > 0) {
			listData.clear();
			for (County c : listCounties) {
				listData.add(c.getCountyName());
			}
			adapter.notifyDataSetChanged();
			lv_area.setSelection(0);
			tv_title.setText(selectedCity.getCityName());
			LEVEL_CURRENT = LEVEL_COUNTY;
		} else {
			queryFromRequest("county", selectedCity.getCityCode());
		}
	}

	//重写返回键方法
	@Override
	public void onBackPressed() {
		switch (LEVEL_CURRENT) {
		case LEVEL_COUNTY:
			loadCity();
			break;
		case LEVEL_CITY:
			loadProvince();
			break;
		default:
			finish();
			break;
		}
	}
	
	
	public void queryFromRequest(final String flag, final String code) {

		String path = "http://www.weather.com.cn/data/list3/city"
				+ (TextUtils.isEmpty(code) ? "" : code) + ".xml";

		LogUtil.i(TAG, "--path->>" + path);

		HttpUtil.sendHttpRequest(path, new CallBackListener() {

			@Override
			public void onFinished(String response) {
				LogUtil.i(TAG, response);
				boolean result = false;
				if ("province".equals(flag)) {
					result = Utility.handleProvincesResponse(db, response);
				} else if ("city".equals(flag)) {
					result = Utility.handleCitiesResponse(db, response, code);
				} else if ("county".equals(flag)) {
					result = Utility.handleCountiesResponse(db, response, code);
				}

				if (result) {
					// 回到主线程更新界面
					runOnUiThread(new Runnable() {
						public void run() {

							if ("province".equals(flag)) {
								loadProvince();
							} else if ("city".equals(flag)) {
								loadCity();
							} else if ("county".equals(flag)) {
								loadCounty();
							}

						}
					});
				}

			}

			@Override
			public void onError(Exception e) {
				LogUtil.i(TAG, e.getMessage());

			}
		});

	}

}
