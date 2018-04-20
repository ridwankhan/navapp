package com.example.database;
import android.arch.persistence.room.*;
import android.util.Log;

import java.util.*;
import com.google.gson.*;

public class DataPointConverters {

//not sure if this separator is correct
public static String strSeparator1 = ",";
public static String strSeparator2 = "\"val\":|\\}";

@TypeConverter
public static ArrayList<Integer> fromString(String dataPointString) {
    ArrayList<Integer> dataPoints = new ArrayList<>();
    String[] split = dataPointString.split(strSeparator2);
    for (int i = 1; i < split.length; i+=2){
        dataPoints.add(Integer.valueOf(split[i]));
    }
    return dataPoints;
}

@TypeConverter
public static String fromArrayList(ArrayList<DataPoint> list) {
    DataPoint[] dataPointArray = new DataPoint[list.size()];
    for(int i = 0;i<list.size();i++){
        dataPointArray[i] = list.get(i);
    }

    String str = "";
    Gson gson = new Gson();
    for (int i = 0; i < dataPointArray.length; i++) {
        String jsonString = gson.toJson(dataPointArray[i]);
        str = str + jsonString;
        if (i < dataPointArray.length - 1) {
            str = str + strSeparator1;
        }
    }
    return str;
}

}

