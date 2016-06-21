package me.fgj.myweatherapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.fgj.myweatherapp.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, AutoUpdateService.class);
		context.startService(i);
	}
	
}
