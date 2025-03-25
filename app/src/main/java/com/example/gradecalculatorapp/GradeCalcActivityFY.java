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

public class GradeCalcActivityFY extends AppCompatActivity {
    Button calcButton;
    EditText[] gradeInputs;
    EditText[] creditInputs;
    TextView gradeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fygrade_calc);

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
                findViewById(R.id.gradeInput7)
        };

        creditInputs = new EditText[]{
                findViewById(R.id.creditInput1),
                findViewById(R.id.creditInput2),
                findViewById(R.id.creditInput3),
                findViewById(R.id.creditInput4),
                findViewById(R.id.creditInput5),
                findViewById(R.id.creditInput6),
                findViewById(R.id.creditInput7)
        };
        // Set button click listener
        calcButton.setOnClickListener(v -> startCalc());
    }

    private void startCalc() {
        ArrayList<Double> L5grades = new ArrayList<>();
        ArrayList<Double> L6credits = new ArrayList<>();
        for (int i = 0; i < gradeInputs.length; i++) {
            String gradeText = gradeInputs[i].getText().toString();
            String creditText = creditInputs[i].getText().toString();

            if (!gradeText.isEmpty() && !creditText.isEmpty()) {
                try {
                    double grade = Double.parseDouble(gradeText);
                    double credit = Double.parseDouble(creditText);

                    L5grades.add(grade);
                    L6credits.add(credit);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid input in row " + (i + 1), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        // Debugging: Show collected values
        Toast.makeText(this, "Grades: " + L5grades + "\nCredits: " + L6credits, Toast.LENGTH_LONG).show();
        GradeCalc GradeCalc = new GradeCalc(L5grades, null, L6credits, null);
        double methodC = GradeCalc.methodCCalc();
        if (methodC > 30.0) {
            gradeText.setText("You Passed: "+methodC);
        } else {
            gradeText.setText("Sorry you failed: "+methodC);
        }
    }

}
