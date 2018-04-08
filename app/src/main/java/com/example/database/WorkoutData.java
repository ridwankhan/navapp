package com.example.database;

import android.arch.persistence.room.*;
import android.text.format.DateFormat;

import java.sql.Timestamp;
import java.util.Date;

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
    @TypeConverters({TimestampConverters.class})
    private Date workoutTimeStamp;

    public String getUserName(){
        return this.userName;
    }

    public int getWorkoutID(){
        return this.getWorkoutID();
    }

    public Date getWorkoutTimeStamp(){
        return this.workoutTimeStamp;
    }

    public void setWorkoutTimeStamp(Date workoutTimeStamp){
        this.workoutTimeStamp = workoutTimeStamp;
    }

    public WorkoutData(int workoutID,String userName){
        this.workoutID = workoutID;
        this.userName = userName;
        //this.workoutTimeStamp = workoutTimeStamp;
        this.workoutTimeStamp = new Date();
        //set Timestamp to curr time
        //set id to be the increment of last ID in table
    }
}