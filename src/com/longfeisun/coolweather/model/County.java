package com.longfeisun.coolweather.model;


public class County {
	private int id;
	private String countyCode;
	private String countyName;
	private String cityCode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCountyCode() {
		return countyCode;
	}

	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}

	public String getCountyName() {
		return countyName;
	}

	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	@Override
	public String toString() {
		return "County [id=" + id + ", countyCode=" + countyCode
				+ ", countyName=" + countyName + ", cityCode=" + cityCode + "]";
	}

}
