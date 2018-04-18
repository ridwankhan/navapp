package com.example.util;

/**
 * Created by Brian on 4/18/2018.
 */

public class ListData {
    String Date;
    Double Score;
    String Muscle;
    String Exercise;
    Integer Set;
    Integer Weight;
    Double Activation;

    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        this.Date = date;
    }

    public Double getScore() {
        return Score;
    }
    public void setScore(Double score) {
        this.Score = score;
    }

    public String getMuscle() {
        return Muscle;
    }
    public void setMuscle(String muscle) {
        Muscle = muscle;
    }

    public String getExercise() {
        return Exercise;
    }
    public void setExercise(String exercise) {
        this.Exercise = exercise;
    }

    public Integer getSet() {
        return Set;
    }
    public void setSet(Integer set) {
        this.Set = set;
    }

    public Integer getWeight() {
        return Weight;
    }
    public void setWeight(Integer weight) {
        this.Weight = weight;
    }

    public Double getActivation() {
        return Activation;
    }
    public void setActivation(Double activation) {
        this.Activation = activation;
    }
}
