package com.longfeisun.coolweather.util;

import android.text.TextUtils;

import com.longfeisun.coolweather.db.CoolWeatherDB;
import com.longfeisun.coolweather.model.City;
import com.longfeisun.coolweather.model.County;
import com.longfeisun.coolweather.model.Province;

public class Utility {

	/**
	 * ��������Ϣ����[ʡ]��Ϣ���浽���ݿ�
	 * 
	 * @param response
	 * @return
	 */
	public static boolean handleProvincesResponse(CoolWeatherDB db,
			String response) {
		// request: http://www.weather.com.cn/data/list3/city.xml
		// response:
		// 01|����,02|�Ϻ�,03|���,04|����,05|������,06|����,07|����,08|���ɹ�,09|�ӱ�,10|ɽ��,11|����,12|ɽ��,13|�½�,14|����,15|�ຣ,16|����,17|����,18|����,19|����,20|����,21|�㽭,22|����,23|����,24|����,25|����,26|����,27|�Ĵ�,28|�㶫,29|����,30|����,31|����,32|���,33|����,34|̨��
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
