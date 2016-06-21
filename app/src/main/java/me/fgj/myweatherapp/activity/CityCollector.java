package me.fgj.myweatherapp.activity;

import java.util.ArrayList;
import java.util.List;

public class CityCollector {
	public static List<CityItem> City = new ArrayList<CityItem>();
	public void addCity(CityItem cityItem) {
		City.add(cityItem);
	}
	public void removeCity(CityItem cityItem) {
		City.remove(cityItem);
	}
	public static Boolean isExist(CityItem cityItem) {
		for (int i = 0; i < City.size(); i++) {
			if (cityItem.cityName.equals(City.get(i).cityName)) {
				return true;
			}
		}
		return false;
	}
}
