package com.longfeisun.coolweather.util;

import android.text.TextUtils;

import com.longfeisun.coolweather.db.CoolWeatherDB;
import com.longfeisun.coolweather.model.City;
import com.longfeisun.coolweather.model.County;
import com.longfeisun.coolweather.model.Province;

public class Utility {

	/**
	 * 
	 * 
	 * @param response
	 * @return
	 */
	public static boolean handleProvincesResponse(CoolWeatherDB db,
			String response) {
		// request: http://www.weather.com.cn/data/list3/city.xml
		// response:
		boolean result = false;
		if (!TextUtils.isEmpty(response)) {
			String[] arr = response.split(",");
			if (arr != null && arr.length > 0) {
				for (String str : arr) {
					String[] strs = str.split("\\|");
					Province province = new Province();
					province.setProvinceCode(strs[0]);
					province.setProvinceName(strs[1]);
					db.saveProvince(province);
				}
				result = true;
			}
		}
		return result;
	}

	public static boolean handleCitiesResponse(CoolWeatherDB db,
			String response, String provinceCode) {
		boolean result = false;
		if (!TextUtils.isEmpty(response)) {
			String[] arr = response.split(",");
			if (arr != null && arr.length > 0) {
				for (String str : arr) {
					String[] strs = str.split("\\|");
					City city = new City();
					city.setCityCode(strs[0]);
					city.setCityName(strs[1]);
					city.setProvinceCode(provinceCode);
					db.saveCity(city);
				}
				result = true;
			}
		}
		return result;
	}

	public static boolean handleCountiesResponse(CoolWeatherDB db,
			String response, String cityCode) {
		boolean result = false;

		if (!TextUtils.isEmpty(response)) {
			String[] arr = response.split(",");
			if (arr != null && arr.length > 0) {
				for (String str : arr) {
					String[] strs = str.split("\\|");
					County county = new County();
					county.setCountyCode(strs[0]);
					county.setCountyName(strs[1]);
					county.setCityCode(cityCode);
					db.saveCounty(county);
				}
				result = true;
			}
		}
		return result;
	}

}
