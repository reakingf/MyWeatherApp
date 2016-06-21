package me.fgj.myweatherapp.activity;

import android.widget.Button;

public class CityItem {
	public String cityName;
	public String weatherInfo;
	public String cityId;
	public Button delete;
	public CityItem(String cityName, String weatherInfo, String cityId) {
		this.cityName = cityName;
		this.weatherInfo = weatherInfo;
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public String getWeatherInfo() {
		return weatherInfo;
	}
	public String getCityId() {
		return cityId;
	}
}
