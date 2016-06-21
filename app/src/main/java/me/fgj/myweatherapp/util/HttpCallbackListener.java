package me.fgj.myweatherapp.util;

public interface HttpCallbackListener {
	void onFinish(String response);
	void onError(Exception e);
}
