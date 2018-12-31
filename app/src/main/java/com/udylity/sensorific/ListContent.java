package com.udylity.sensorific;

import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListContent {
	public static final List<String> ITEMS = new ArrayList<String>();

	public static final Map<Integer, String> ITEM_MAP = new HashMap<Integer, String>();


	static {
        int counter = 0;
        for(Sensor s : SensListActivity.deviceSensors){
            counter++;
            ITEM_MAP.put(counter, s.getName());
            ITEMS.add(s.getName());
        }
	}

}
