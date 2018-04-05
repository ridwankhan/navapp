package com.example.database;

import android.arch.persistence.room.*;
import java.sql.Timestamp;
import java.util.*;

@Entity(
    tableName="SetDataTable",
    indices= arrayOf(Index(value = "exerciseID", name = "exercise_index"))
)

public class SetData{
    //unique incrementing
    @PrimaryKey
    private final int setID;

    //ID of the exercise this is a component of, e.g. dumbbell flys
    @ForeignKey(entity = ExerciseData.class,parentColumns="exerciseID", childColumns="exercise_id")
    private final int exerciseID;

    //this class is a wrapper for timestamp SQL recognizes
    @ColumnInfo(name="timestamp")
    private final Timestamp setTimeStamp;

    //1st set, 2nd set, etc.
    @ColumnInfo(name="set_number")
    private int setNumber;

    //amount of weight lifted
    @ColumnInfo(name="weight")
    private int weight;

    //holds the data values
    @ColumnInfo(name="data_values")
    private ArrayList<Integer> setDataValues;

    //need a constructor for the table
    public SetData(int setID,int exerciseID, Timestamp setTimeStamp, int weight, int setNumber, ArrayList<Integer> setDataValues){
        //set Timestamp to curr time
        this.setID = setID;
        this.exerciseID = exerciseID;
        this.setTimeStamp = setTimeStamp;
        this.weight = weight;
        this.setNumber = setNumber;
        this.setDataValues.addAll(setDataValues);
        //set id to be the increment of last ID in table
    }
}
