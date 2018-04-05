package com.example.database;
import android.arch.persistence.room.*;

@Database(entities = {SetData.class, WorkoutData.class, ExerciseData.class }, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ExerciseDao exerciseDao();
}
