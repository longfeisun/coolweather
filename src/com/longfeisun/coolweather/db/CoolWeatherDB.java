package com.longfeisun.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.longfeisun.coolweather.model.City;
import com.longfeisun.coolweather.model.County;
import com.longfeisun.coolweather.model.Province;

public class CoolWeatherDB {
	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "coolweather.db";

	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;

	public CoolWeatherDB(Context context) {
		CoolWeatherDBOpenHelper helper = new CoolWeatherDBOpenHelper(context,
				DB_NAME, null, DB_VERSION);
		db = helper.getWritableDatabase();
	}

	public static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}

	/**
	 * 保存省信息到数据库
	 * 
	 * @param province
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_code", province.getProvinceCode());
			values.put("province_name", province.getProvinceName());
			db.insert("province", null, values);
		}
	}

	/**
	 * 保存城市信息到数据库
	 * 
	 * @param city
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_code", city.getCityCode());
			values.put("city_name", city.getCityName());
			values.put("province_code", city.getProvinceCode());
			db.insert("city", null, values);
		}
	}

	/**
	 * 保存区信息到数据库
	 * 
	 * @param county
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_code", county.getCountyCode());
			values.put("county_name", county.getCountyName());
			values.put("city_code", county.getCityCode());
			db.insert("county", null, values);
		}
	}

	/**
	 * 取省份列表
	 * 
	 * @return
	 */
	public List<Province> getAllProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			Province province;
			do {
				province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 按省份编号取城市列表
	 * 
	 * @param provinceCode
	 * @return
	 */
	public List<City> getCitiesbyProvinceCode(String provinceCode) {
		List<City> list = new ArrayList<City>();

		Cursor cursor = db.query("city", null, " province_code = ? ",
				new String[] { provinceCode }, null, null, null);
		if (cursor.moveToFirst()) {
			City city = null;
			do {
				city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * 按城市编码取区列表
	 * @param cityCode
	 * @return
	 */
	public List<County> getCountiesByCityCode(String cityCode) {
		List<County> list = new ArrayList<>();
		Cursor cursor = db.query("county", null, "city_code = ?",
				new String[] { cityCode }, null, null, null);
		if (cursor.moveToFirst()) {
			County county = null;
			do {
				county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}

}
