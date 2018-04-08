package com.example.database;

import android.arch.persistence.room.*;

@Entity(tableName="dataPoint")

public class DataPoint{
    //unique incrementing
    @PrimaryKey(autoGenerate= true)
    private int dataID;

    @ColumnInfo(name="val")
    private final int val;

    //this class is a wrapper for timestamp SQL recognizes
    @ColumnInfo(name="timestamp")
    @TypeConverters({TimestampConverters.class})
    private long dataTimeStamp;

    public int getVal(){
        return this.val;
    }

    public long getDataTimeStamp(){
        return this.dataTimeStamp;
    }

    public int getDataID(){
        return this.dataID;
    }

    public void setDataID(int dataID){
        this.dataID = dataID;
    }

    //need a constructor for the table
    public DataPoint(int val, long dataTimeStamp){
        //set Timestamp to curr time
        this.val = val;
        this.dataTimeStamp = dataTimeStamp;
        //set id to be the increment of last ID in table
    }
}
