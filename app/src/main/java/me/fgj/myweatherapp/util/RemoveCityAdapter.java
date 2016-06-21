package me.fgj.myweatherapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import me.fgj.myweatherapp.R;
import me.fgj.myweatherapp.activity.CityCollector;
import me.fgj.myweatherapp.activity.CityItem;

public class RemoveCityAdapter extends ArrayAdapter<CityItem> {
	private int resourceId;
	public RemoveCityAdapter(Context context, int textViewResourceId,
			List<CityItem> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CityItem cityItem = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.cityName = (TextView) view.findViewById(R.id.city_name);
			viewHolder.tempScope = (TextView) view.findViewById(R.id.temp_scope);
			viewHolder.remove = (Button) view.findViewById(R.id.delete);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.cityName.setText(cityItem.getCityName());
		viewHolder.tempScope.setText(cityItem.getWeatherInfo());
		viewHolder.remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CityCollector.City.remove(position);
				notifyDataSetChanged();
			}
		});
		return view;
	}
	class ViewHolder {
		TextView cityName;
		TextView tempScope;
		Button remove;
	}
}
