package com.example.database;

import android.arch.persistence.room.*;

@Entity(tableName="dataPoint")

public class DataPoint{
    //unique incrementing
    @ColumnInfo(name="val")
    private final int val;

    //this class is a wrapper for timestamp SQL recognizes
    @ColumnInfo(name="timestamp")
    private final long dataTimeStamp;

    //need a constructor for the table
    public DataPoint(int val, long dataTimeStamp){
        //set Timestamp to curr time
        this.val = val;
        this.dataTimeStamp = dataTimeStamp;
        //set id to be the increment of last ID in table
    }
}
