package me.fgj.myweatherapp.activity;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.fgj.myweatherapp.R;
import me.fgj.myweatherapp.service.AutoUpdateService;
import me.fgj.myweatherapp.util.HttpCallbackListener;
import me.fgj.myweatherapp.util.HttpUtil;
import me.fgj.myweatherapp.util.Utility;

/**
 * @author reakingf
 * 下拉效果有Bug
 */
public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;
	private LinearLayout weatherBackground;
	private TextView cityNameText;
	private TextView publishText;
	private TextView weatherDespText;
	private TextView temp1Text;
	private TextView temp2Text;
	private TextView currentDateText;
	Button switchCity;
	Button refreshWeather;
	Button setting;
	private SwipeRefreshLayout swipeRefreshLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id
				.weather_info_layout);
		weatherBackground = (LinearLayout) findViewById(R.id.
				weather_background);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyCode = getIntent().getStringExtra("county_code");
		String cityId = getIntent().getStringExtra("cityIdFromConcerned");
		if (!TextUtils.isEmpty(cityId)) {
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherInfo(cityId);
		} else if (!TextUtils.isEmpty(countyCode)) {
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			showWeather();
		}
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		setting = (Button) findViewById(R.id.setting);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		setting.setOnClickListener(this);
		//下拉刷新
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById
				(R.id.swipeRefreshLayout);
		//设置卷内的颜色
		swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
				android.R.color.holo_green_light, android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
		//设置下拉刷新监听
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						publishText.setText("同步中...");
						SharedPreferences prefs = PreferenceManager
								.getDefaultSharedPreferences(WeatherActivity.
										this);
						String weatherCode = prefs.getString("weather_code", "");
						if (!TextUtils.isEmpty(weatherCode)) {
							queryWeatherInfo(weatherCode);
						}
						//停止刷新动画
						swipeRefreshLayout.setRefreshing(false);
					}
				}, 1000);
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(WeatherActivity.this,
					ConcernedCityActivity.class);
//			Intent intent = new Intent(this, ChooseAreaActivity.class);
//			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中...");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences
					(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			break;
		case R.id.setting:
			Intent intent2 = new Intent(this, SettingActivity.class);
			startActivity(intent2);
			finish();
		default:
			break;
		}
	}
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city" + 
	countyCode + ".xml";
		queryFromServer(address, "countyCode");
	}
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/adat/cityinfo/" + 
	weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}

	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						//从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if (array != null && array.length == 2) {
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				} else if ("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this,
							response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.
				getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp2", ""));
		temp2Text.setText(prefs.getString("temp1", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天" + prefs.getString("publish_time", "") + "发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		setBackground_weather();
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}
	/**
	 * 根据相应天气修改背景图片，这里这给出了晴天、多云、阴天的情况，其他情况如小雨，多云转阵雨等
	 * 均为给出
	 */
	private void setBackground_weather() {
	SharedPreferences prefs = PreferenceManager.
			getDefaultSharedPreferences(this);
		Calendar calendar  = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if ("阴".equals(prefs.getString("weather_desp", "")) ||
				"多云".equals(prefs.getString("weather_desp", ""))) {
			if (hour <= 17) {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.cloudy_day));
			} else {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.cloudy_night));
			}
		} else if ("晴".equals(prefs.getString("weather_desp", ""))) {
			if (hour <= 17) {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.sunny_day));
			} else {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.sunny_night));
			}
		} else if ("小雨".equals(prefs.getString("weather_desp", ""))) {
			if (hour <= 17) {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.littlerian_day));
			} else {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.littlerian_night));
			}
		} else if ("阵雨".equals(prefs.getString("weather_desp", ""))||
				"雷阵雨".equals(prefs.getString("weather_desp", ""))) {
			if (hour <= 17) {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.shower_day));
			} else {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.shower_night));
			}
		} else if ("暴雨".equals(prefs.getString("weather_desp", ""))) {
			if (hour <= 17) {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.rianstrom_day));
			} else {
				weatherBackground.setBackground(getResources().getDrawable(
						R.drawable.rianstrom_night));
			}
		}
	}
}
