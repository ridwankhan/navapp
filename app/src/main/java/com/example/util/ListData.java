package com.example.util;

/**
 * Created by Brian on 4/18/2018.
 */

public class ListData {
    String Date;
    String Muscle;
    String Exercise;
    Integer Set;
    Integer Weight;
    Double Activation;
    Integer SetId;

    public String getDate() {
        return Date;
    }
    public void setDate(String date) {
        this.Date = date;
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

    public Integer getSetId() {
        return SetId;
    }
    public void setSetId(Integer setId) {
        this.SetId = setId;
    }
}
