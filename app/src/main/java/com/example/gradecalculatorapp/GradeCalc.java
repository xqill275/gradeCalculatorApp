package com.example.gradecalculatorapp;

import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class GradeCalc {
    private ArrayList<Double> LV5Grades;
    private ArrayList<Double> LV6Grades;
    private ArrayList<Double> LV5Credits;
    private ArrayList<Double> LV6Credits;
    public GradeCalc() {
        this.LV5Grades = new ArrayList<Double>();
        this.LV6Grades = new ArrayList<Double>();
        this.LV5Credits = new ArrayList<Double>();
        this.LV6Credits = new ArrayList<Double>();

    }

    public void setLV5Grades(ArrayList<Double> newLV5Grades) {
        this.LV5Grades = newLV5Grades;
    }

    public void setLV5Credits(ArrayList<Double> newLV5Credits) {
        this.LV5Credits = newLV5Credits;
    }

    public void setLV6Grades(ArrayList<Double> newLV6Grades) {
        this.LV6Grades = newLV6Grades;
    }

    public void setLV6Credits(ArrayList<Double> newLV6Credits) {
        this.LV5Credits = newLV6Credits;
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

    public ArrayList<Double> getArray(EditText[] array) {
        ArrayList<Double> newArray = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            String arrayText = array[i].getText().toString();
            if (!arrayText.isEmpty()) {
                try {
                    double doubleText = Double.parseDouble(arrayText);

                    newArray.add(doubleText);
                } catch (NumberFormatException e) {
                    //Toast.makeText(this, "Invalid input in row " + (i + 1), Toast.LENGTH_SHORT).show();
                    return null;
                }

            }
        }
        return newArray;
    }
}
