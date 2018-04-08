package com.example.database;
import android.arch.persistence.room.*;

public class Converters {

//not sure if this separator is correct
public static String strSeparator = "__,__";

@TypeConverter
public static ArrayList<DataPoint> fromString(String dataPointString) {
    String[] dataPointArray = dataPointString.split(strSeparator);
    ArrayList<DataPoint>  = new ArrayList<DataPoint>();
    Gson gson = new Gson();
    for (int i=0;i<dataPointArray.length-1;i++){
        videos.add(gson.fromJson(dataPointArray[i] , DataPoint.class));
    }
    return videos;
}

@TypeConverter
public static String fromArrayList(ArrayList<DataPoint> list) {
    DataPoint[] dataPointArray = new DataPoint[list.size()];
    for(int i = 0;i<=list.size()-1;i++){
        DataPoint[i] = list.get(i);
    }

    String str = "";
    Gson gson = new Gson();
    for (int i = 0; i < videoArray.length; i++) {
        String jsonString = gson.toJson(videoArray[i]);
        str = str + jsonString;
        if (i < videoArray.length - 1) {
            str = str + strSeparator;
        }
    }
    return str;
}

}
