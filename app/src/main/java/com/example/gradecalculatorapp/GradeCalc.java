package com.example.gradecalculatorapp;

import java.util.ArrayList;

public class GradeCalc {
    private ArrayList<Double> LV5Grades;
    private ArrayList<Double> LV6Grades;
    private ArrayList<Double> LV5Credits;
    private ArrayList<Double> LV6Credits;
    public GradeCalc(ArrayList<Double> LV5Grades, ArrayList<Double> LV6Grades, ArrayList<Double> LV5Credits, ArrayList<Double> LV6Credits) {
        this.LV5Grades = LV5Grades;
        this.LV6Grades = LV6Grades;
        this.LV5Credits = LV5Credits;
        this.LV6Credits = LV6Credits;
    }

    public double methodCCalc(){
        return getWeightedAverage(LV5Grades, LV5Credits);
    }

    public double methodACalc(){
        double LV5average = getWeightedAverage(LV5Grades, LV5Credits);
        double LV6average = getWeightedAverage(LV6Grades, LV6Credits);
        return (LV5average + LV6average) / 2;
    }

    public double methodBCalc(){
        double LV5average = getWeightedAverage(LV5Grades, LV5Credits);
        double LV6average = getWeightedAverage(LV6Grades, LV6Credits);
        return (LV5average + LV6average + LV6average) / 3;
    }

    private double getWeightedAverage(ArrayList<Double> inputGrades, ArrayList<Double> inputCredits) {
        double weightedSum = 0;
        double totalCredits = 0;

        for (int i = 0; i < inputGrades.size(); i++) {
            weightedSum += inputGrades.get(i) * inputCredits.get(i);
            totalCredits += inputCredits.get(i);
        }

        if (totalCredits == 0) return 0; // Prevent division by zero
        return weightedSum / totalCredits; // Weighted average formula
    }
}
