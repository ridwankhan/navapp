package com.example.database;
import android.arch.persistence.room.*;

public class Converters {
@TypeConverter
public static ArrayList<Integer> fromString(String value) {
    Type listType = new TypeToken<ArrayList<String>>() {}.getType();
    return new Gson().fromJson(value, listType);
}

@TypeConverter
public static String fromArrayList(ArrayList<Integer> list) {
    Gson gson = new Gson();
    String json = gson.toJson(list);
    return json;
}
}
