package com.example.database;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConverters {


    static DateFormat df = new SimpleDateFormat();

    @TypeConverter
    public static Date fromString(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String fromTimestamp(Date dateVal) {
        if (dateVal != null) {
            return dateVal.toString();
        } else {
            return null;
        }
    }
}
