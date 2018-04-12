package com.example.database;

import android.arch.persistence.room.*;
import java.sql.Timestamp;
import java.util.*;

@Entity(
    tableName="SetDataTable",
    indices= {@Index(value = "exerciseID", name = "exercise_index")}
)

public class SetData{
    //unique incrementing
    @PrimaryKey
    private final int setID;

    //ID of the exercise this is a component of, e.g. dumbbell fly
    @ForeignKey(entity = ExerciseData.class,parentColumns="exerciseID", childColumns="exercise_id")
    private final int exerciseID;

    //this class is a wrapper for timestamp SQL recognizes
    @ColumnInfo(name="timestamp")
    @TypeConverters({TimestampConverters.class})
    private Date setTimeStamp;

    //1st set, 2nd set, etc.
    @ColumnInfo(name="set_number")
    private int setNumber;

    //amount of weight lifted
    @ColumnInfo(name="weight")
    private int weight;

    //holds the data values
    private String setDataValues;

    public int getWeight(){
        return this.weight;
    }

    public int getSetNumber(){
        return this.setNumber;
    }

    public int getExerciseID(){
        return this.exerciseID;
    }

    public int getSetID(){
        return this.setID;
    }

    public String getSetDataValues(){
        return this.setDataValues;
    }

    public Date getSetTimeStamp(){
        return this.setTimeStamp;
    }

    public void setSetTimeStamp(Date setTimeStamp){
        this.setTimeStamp = setTimeStamp;
    }

    //need a constructor for the table
    public SetData(int setID, int exerciseID, int weight, int setNumber, String setDataValues){
        //set Timestamp to curr time
        this.setID = setID;
        this.setDataValues = setDataValues;
        this.exerciseID = exerciseID;
        this.setTimeStamp = new Timestamp(System.currentTimeMillis());
        this.weight = weight;
        this.setNumber = setNumber;
        //set id to be the increment of last ID in table
    }
}
