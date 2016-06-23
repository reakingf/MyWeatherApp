package me.fgj.myweatherapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import me.fgj.myweatherapp.R;
import me.fgj.myweatherapp.util.RemoveCityAdapter;

public class RemoveCityActivity extends Activity implements OnClickListener {
	ListView removeListView;
	ListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.remove_city_layout);
		adapter = new RemoveCityAdapter(RemoveCityActivity.this, R.layout.
				remove_city_item, CityCollector.City);
		removeListView = (ListView) findViewById(R.id.remove_ciy_item);
		removeListView.setAdapter(adapter);
		Button cancel = (Button) findViewById(R.id.cancel);
		Button determine = (Button) findViewById(R.id.determine);
		cancel.setOnClickListener(this);
		determine.setOnClickListener(this);
 	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			Intent intent = new Intent(RemoveCityActivity.this, ConcernedCityActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.determine:
			Intent intent2 = new Intent(RemoveCityActivity.this, ConcernedCityActivity.class);
			startActivity(intent2);
			finish();
			break;
		default:
			break;
		}
	}
}
