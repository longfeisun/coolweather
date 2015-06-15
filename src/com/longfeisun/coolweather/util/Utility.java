package com.longfeisun.coolweather.util;

import android.text.TextUtils;

import com.longfeisun.coolweather.db.CoolWeatherDB;
import com.longfeisun.coolweather.model.City;
import com.longfeisun.coolweather.model.County;
import com.longfeisun.coolweather.model.Province;

public class Utility {

	/**
	 * 处理返回信息，将[省]信息保存到数据库
	 * 
	 * @param response
	 * @return
	 */
	public static boolean handleProvincesResponse(CoolWeatherDB db,
			String response) {
		// request: http://www.weather.com.cn/data/list3/city.xml
		// response:
		// 01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,09|河北,10|山西,11|陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳门,34|台湾
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
