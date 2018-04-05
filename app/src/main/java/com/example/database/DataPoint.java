package com.example.database;

import android.arch.persistence.room.*;
import java.sql.Timestamp;
import java.util.*;

@Entity(
    tableName="SetDataTable",
    indices= arrayOf(Index(value = "exerciseID", name = "exercise_index"))
)

public class DataPoint{
    //unique incrementing
    @PrimaryKey
    private final int val;

    //this class is a wrapper for timestamp SQL recognizes
    @ColumnInfo(name="timestamp")
    private final Timestamp dataTimeStamp;

    //need a constructor for the table
    public DataPoint(int val, Timestamp dataTimeStamp){
        //set Timestamp to curr time
        this.val = val;
        this.dataTimeStamp = dataTimeStamp;
        //set id to be the increment of last ID in table
    }
}
