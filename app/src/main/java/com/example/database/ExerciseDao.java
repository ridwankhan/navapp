package com.example.database;

import android.arch.persistence.room.*;
import java.util.*;


@Dao
public interface ExerciseDao {
    @Query("SELECT data_values FROM SetDataTable WHERE setID = :setID ")
    ArrayList<Integer> getSetData(int setID);

    @Query("SELECT setID FROM SetDataTable ORDER BY setID DESC")
    int getHighestID();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertSetData(SetData setData);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertExerciseData(WorkoutData workoutData1);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertWorkoutData(WorkoutData workoutData1);
}