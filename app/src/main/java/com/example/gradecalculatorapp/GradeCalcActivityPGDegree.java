package com.example.gradecalculatorapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class GradeCalcActivityPGDegree extends AppCompatActivity {
    Button calcButton;
    EditText[] gradeInputs;
    EditText[] creditInputs;
    TextView gradeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grade_calc_pgaward);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        gradeText = findViewById(R.id.gradeTextId);
        // Initialize button
        calcButton = findViewById(R.id.CalcGradeID);

        // Initialize EditText fields
        gradeInputs = new EditText[]{
                findViewById(R.id.gradeInput1),
                findViewById(R.id.gradeInput2),
                findViewById(R.id.gradeInput3),
                findViewById(R.id.gradeInput4),
                findViewById(R.id.gradeInput5),
                findViewById(R.id.gradeInput6),
                findViewById(R.id.gradeInput7),
        };

        creditInputs = new EditText[]{
                findViewById(R.id.creditInput1),
                findViewById(R.id.creditInput2),
                findViewById(R.id.creditInput3),
                findViewById(R.id.creditInput4),
                findViewById(R.id.creditInput5),
                findViewById(R.id.creditInput6),
                findViewById(R.id.creditInput7),
        };
        // Set button click listener
        calcButton.setOnClickListener(v -> startCalc());

        Button goBack = findViewById(R.id.GoBackButton);
        goBack.setOnClickListener(view -> finish());
    }

    public void startCalc() {
        GradeCalc gradeCalc = new GradeCalc();
        ArrayList<Double> Grades = gradeCalc.getArray(gradeInputs);
        ArrayList<Double> Credits = gradeCalc.getArray(creditInputs);
        gradeCalc.setLV6Credits(Credits);
        gradeCalc.setLV6Grades(Grades);

        // Debugging: Show collected values
        Toast.makeText(this, "Grades: " + Grades + "\nCredits: " + Credits, Toast.LENGTH_LONG).show();
        double methodC = gradeCalc.methodCCalc();
        if (methodC > 30.0) {
            gradeText.setText("You Passed: "+methodC);
        } else {
            gradeText.setText("Sorry you failed: "+methodC);
        }
    }
}