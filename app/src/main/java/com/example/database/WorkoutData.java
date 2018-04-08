package com.example.database;

import android.arch.persistence.room.*;
import java.sql.Timestamp;

@Entity(tableName="WorkoutDataTable")
public class WorkoutData{
    //unique incrementing
    @PrimaryKey
    private final int workoutID;

    //Kind of hackish solution, just to allow us to put our names down
    @ColumnInfo(name="user_name")
    private String userName;

    //this class is a wrapper for timestamp SQL recognizes
    @ColumnInfo(name="timestamp")
    private final Timestamp workoutTimeStamp;

    public WorkoutData(int workoutID,String userName){
        this.workoutID = workoutID;
        this.userName = userName;
        //this.workoutTimeStamp = workoutTimeStamp;
        this.workoutTimeStamp = new Timestamp(System.currentTimeMillis());
        //set Timestamp to curr time
        //set id to be the increment of last ID in table
    }
}