package me.fgj.myweatherapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import me.fgj.myweatherapp.R;
import me.fgj.myweatherapp.util.CityAdapter;

public class ConcernedCityActivity extends Activity
	implements OnClickListener {
	ListView listView;
	CityAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_city_layout);
		inits();
		adapter = new CityAdapter(this, R.layout.city_item,
				CityCollector.City);
		listView = (ListView) findViewById(R.id.ciy_item);
		listView.setAdapter(adapter);
		Button back  =(Button) findViewById(R.id.back_from_more_city);
		Button edit = (Button) findViewById(R.id.remove_city);
		Button add = (Button) findViewById(R.id.add_city);
		back.setOnClickListener(this);
		edit.setOnClickListener(this);
		add.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CityItem cityItem = CityCollector.City.get(position);
				String cityId = cityItem.cityId;
				Intent intent = new Intent(ConcernedCityActivity.this,
						WeatherActivity.class);
				intent.putExtra("cityIdFromConcerned", cityId);
				startActivity(intent);
				finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_from_more_city:
			Intent intent = new Intent(ConcernedCityActivity.this,
					WeatherActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.remove_city:
			Intent intent1 = new Intent(ConcernedCityActivity.this,
					RemoveCityActivity.class);
			startActivity(intent1);
			finish();
			break;
		case R.id.add_city:
			Intent intent2 = new Intent(ConcernedCityActivity.this,
					ChooseAreaActivity.class);
			intent2.putExtra("from_concerned_city", true);
			startActivity(intent2);
			finish();
			break;
		default:
			break;
		}
	}
	
	void inits() {
		SharedPreferences prefs = PreferenceManager.
				getDefaultSharedPreferences(this);
		if (prefs.getBoolean("city_selected", false)) {
			CityItem first = new CityItem(prefs.getString("city_name", ""),
					prefs.getString("temp2", "") + "/" + 
			prefs.getString("temp1", ""), prefs.getString("weather_code", ""));
			if (!CityCollector.isExist(first)) {
				CityCollector.City.add(first);
			}
		}
	}
}
