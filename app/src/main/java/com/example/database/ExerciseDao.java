package com.example.database;

import android.arch.persistence.room.*;
import java.util.*;

@Dao
public interface ExerciseDao {
    @Query("SELECT * FROM SetDataTable WHERE setID = :setID ")
    @TypeConverters({TimestampConverters.class})
    SetData getSetData(int setID);

    @Query("SELECT * FROM ExerciseDataTable WHERE exerciseID = :exerciseId ")
    @TypeConverters({TimestampConverters.class})
    ExerciseData getExerciseData(int exerciseId);

    @Query("SELECT * FROM SetDataTable")
    @TypeConverters({TimestampConverters.class})
    SetData[] getSets();

    @Query("SELECT setDataValues FROM SetDataTable WHERE setID = :setID ")
    String getSetDataValuesStr(int setID);

    @Query("SELECT setID FROM SetDataTable ORDER BY setID DESC")
    int getHighestSetID();

    @Query("SELECT exerciseID FROM ExerciseDataTable ORDER BY exerciseID DESC")
    int getHighestExerciseID();

    @Query("SELECT AVG(peak_avg) FROM SetDataTable WHERE exerciseID = :exerciseId")
    int getOverallActivation(int exerciseId);

    /*@Query("SELECT workoutID FROM WorkoutDataTable ORDER BY workoutID DESC")
    int getHighestWorkoutID();*/




    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertSetData(SetData setData);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertExerciseData(ExerciseData exerciseData);

    /*@Insert(onConflict = OnConflictStrategy.ABORT)
    void insertWorkoutData(WorkoutData workoutData);*/
}