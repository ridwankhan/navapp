package com.example.database;
import android.arch.persistence.room.*;

import java.util.*;
import com.google.gson.*;

public class DataPointConverters {

//not sure if this separator is correct
public static String strSeparator = "__,__";

@TypeConverter
public static ArrayList<DataPoint> fromString(String dataPointString) {
    String[] dataPointArray = dataPointString.split(strSeparator);
    ArrayList<DataPoint> dataPoints = new ArrayList<>();
    Gson gson = new Gson();
    for (int i=0;i<dataPointArray.length;i++){
        dataPoints.add(gson.fromJson(dataPointArray[i] , DataPoint.class));
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
            str = str + strSeparator;
        }
    }
    return str;
}

}

