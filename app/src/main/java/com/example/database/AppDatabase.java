package com.example.database;
import android.arch.persistence.room.*;

@Database(entities = {SetData.class, WorkoutData.class, ExerciseData.class , DataPoint.class}, version = 1,exportSchema = false)
@TypeConverters({TimestampConverters.class,DataPointConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExerciseDao exerciseDao();
}


