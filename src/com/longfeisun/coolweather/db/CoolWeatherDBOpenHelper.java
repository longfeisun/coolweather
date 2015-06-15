package com.longfeisun.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherDBOpenHelper extends SQLiteOpenHelper {

	private static final String CREATE_PROVINCE = " create table province( "
			+ " id integer primary key autoincrement,  "
			+ " province_code text, " + " province_name text )";
	private static final String CREATE_CITY = " create table city( "
			+ " id integer primary key autoincrement,  " + " city_code text, "
			+ " city_name text ," + " province_code text )";
	private static final String CREATE_COUNTY = " create table county( "
			+ " id integer primary key autoincrement,  "
			+ " county_code text ," + " county_name text ,city_code)";

	public CoolWeatherDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
