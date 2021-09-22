package com.example.snoremore;

public class ExerciseSample {
    private double distance;
    private double calories;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "ExerciseSample{" +
                "distance=" + distance +
                ", calories=" + calories +
                '}';
    }
}
