package com.example.snoremore;

public class HWeightSample {
    private double height;
    private double weight;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Height & Weight Sample{" +
                "height=" + height +
                ", weight=" + weight +
                '}';
    }
}
