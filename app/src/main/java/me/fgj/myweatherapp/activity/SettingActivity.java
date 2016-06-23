package me.fgj.myweatherapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import me.fgj.myweatherapp.R;
import me.fgj.myweatherapp.service.AutoUpdateService;

public class SettingActivity extends Activity {
	Button back;
	private List<String> settingList = new ArrayList<>();
	ArrayAdapter<String> adapter;
	ListView listView;
	private String[] yesOrNot = new String[]{"开启", "禁止"};
	private String[] frequency = new String[]{"0.5","1","2","4","8"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		back = (Button) findViewById(R.id.setting_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,
						WeatherActivity.class);
				startActivity(intent);
				finish();
			}
		});
		initList();
		listView = (ListView) findViewById(R.id.setting_list_view);
		adapter = new ArrayAdapter<>(SettingActivity.this,
				android.R.layout.simple_list_item_1, settingList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch ((int)id) {
				case 0:
					showDialog();
					break;
				case 1:
					setTime();
					break;
				case 2:
					Toast.makeText(SettingActivity.this, getString(R.string.updateInfo),
							Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
			
		});
	}
	private void showDialog() {
		new AlertDialog.Builder(this).setTitle(getString(R.string.allowUpdate)).
		setItems(yesOrNot,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							Intent intent = new Intent(SettingActivity.this,
									AutoUpdateService.class);
							startService(intent);
							Toast.makeText(SettingActivity.this, getString(R.string.update_opened),
									Toast.LENGTH_SHORT).show();
							break;
						case 1:
							Intent intent2 = new Intent(SettingActivity.this,
									AutoUpdateService.class);
							stopService(intent2);
							Toast.makeText(SettingActivity.this, getString(R.string.update_closed),
									Toast.LENGTH_SHORT).show();
							break;
						default:
							break;
						}
					}
				}).setNegativeButton("Cancel", new DialogInterface
						.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
	private void setTime() {
		new AlertDialog.Builder(this).setTitle(getString(R.string.set_frequency)).
		setItems(frequency,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(SettingActivity.this,
								AutoUpdateService.class);
						switch (which) {
						case 0:
							intent.putExtra("frequency", 0.5);
							Toast.makeText(SettingActivity.this,
									getString(R.string.half_hour),Toast.LENGTH_SHORT).
									show();
							break;
						case 1:
							intent.putExtra("frequency", 1.0);
							Toast.makeText(SettingActivity.this,
									getString(R.string.one_hour),Toast.LENGTH_SHORT).
									show();
							break;
						case 2:
							intent.putExtra("frequency", 2.0);
							Toast.makeText(SettingActivity.this,
									getString(R.string.two_hours),Toast.LENGTH_SHORT).
									show();
							break;
						case 3:
							intent.putExtra("frequency", 4.0);
							Toast.makeText(SettingActivity.this,
									getString(R.string.four_hours),Toast.LENGTH_SHORT).
									show();
							break;
						case 4:
							intent.putExtra("frequency", 8.0);
							Toast.makeText(SettingActivity.this,
									getString(R.string.four_hours),Toast.LENGTH_SHORT).
									show();
							break;
						default:
							break;
						}
						startService(intent);
					}
				}).setNegativeButton("Cancel", new DialogInterface
						.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
	private void initList() {
		String autoRefreshing = getString(R.string.auto_update);
		settingList.add(autoRefreshing);
		String refreshFrequency = getString(R.string.update_frequency);
		settingList.add(refreshFrequency);
		String newVersion = getString(R.string.version_update);
		settingList.add(newVersion);
	}
}
