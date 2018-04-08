package com.example.database;

import android.arch.persistence.room.*;
import java.sql.Timestamp;

@Entity(
        tableName="ExerciseDataTable"//,
        //indices= {@Index(value = "workoutID", name = "workout_index")}
)

public class ExerciseData{
    //unique incrementing
    @PrimaryKey
    private int exerciseID;

    /*//ID of the exercise this is a component of, e.g. dumbbell fly
    @ForeignKey(entity=WorkoutData.class,parentColumns="workoutID",childColumns = "workoutID")
    private final int workoutID;*/

    //this class is a wrapper for timestamp SQL recognizes
    @ColumnInfo(name="time_stamp")
    private final Timestamp exerciseTimeStamp;

    //1st set, 2nd set, etc.
    @ColumnInfo(name="muscle_group")
    private String muscleGroup;

    //name of exercise
    @ColumnInfo(name="name")
    private String exerciseName;

    public void setExerciseID( int exerciseID){
        this.exerciseID = exerciseID;
    }

    public int exerciseID(){
        return this.exerciseID;
    }

    public ExerciseData(int exerciseID,/*int workoutID,*/ String muscleGroup, String exerciseName ){
        this.exerciseID = exerciseID;
        //this.workoutID = workoutID;
        this.exerciseTimeStamp = new Timestamp(System.currentTimeMillis());
        this.muscleGroup = muscleGroup;
        this.exerciseName = exerciseName;

        //set Timestamp to curr time
        //set id to be the increment of last ID in table
    }
}
