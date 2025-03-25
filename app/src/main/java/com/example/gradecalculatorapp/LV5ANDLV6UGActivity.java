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

public class LV5ANDLV6UGActivity extends AppCompatActivity {
    Button calcButton;
    TextView gradeText;
    TextView methodAText;
    TextView methodBText;
    EditText[] LV5gradeInputs;
    EditText[] LV5creditInputs;
    EditText[] LV6gradeInputs;
    EditText[] LV6creditInputs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lv5_andlv6_ugactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        calcButton = findViewById(R.id.CalcGradeID);
        methodAText = findViewById(R.id.MethodATextId);
        methodBText = findViewById(R.id.MethodBTextId);

        LV5gradeInputs = new EditText[]{
                findViewById(R.id.gradeInput1),
                findViewById(R.id.gradeInput2),
                findViewById(R.id.gradeInput3),
                findViewById(R.id.gradeInput4),
                findViewById(R.id.gradeInput5),
                findViewById(R.id.gradeInput6),
                findViewById(R.id.gradeInput7)
        };

        LV5creditInputs = new EditText[]{
                findViewById(R.id.creditInput1),
                findViewById(R.id.creditInput2),
                findViewById(R.id.creditInput3),
                findViewById(R.id.creditInput4),
                findViewById(R.id.creditInput5),
                findViewById(R.id.creditInput6),
                findViewById(R.id.creditInput7)
        };

        LV6gradeInputs = new EditText[]{
                findViewById(R.id.LV6gradeInput1),
                findViewById(R.id.LV6gradeInput2),
                findViewById(R.id.LV6gradeInput3),
                findViewById(R.id.LV6gradeInput4),
                findViewById(R.id.LV6gradeInput5),
                findViewById(R.id.LV6gradeInput6),
                findViewById(R.id.LV6gradeInput7)
        };

        LV6creditInputs = new EditText[]{
                findViewById(R.id.LV6creditInput1),
                findViewById(R.id.LV6creditInput2),
                findViewById(R.id.LV6creditInput3),
                findViewById(R.id.LV6creditInput4),
                findViewById(R.id.LV6creditInput5),
                findViewById(R.id.LV6creditInput6),
                findViewById(R.id.LV6creditInput7)
        };

        calcButton.setOnClickListener(v -> startCalc());
    }

    private void startCalc(){
        ArrayList<Double> L5Grades = getArray(LV5gradeInputs);
        ArrayList<Double> L5Credits = getArray(LV5creditInputs);
        ArrayList<Double> L6Grades = getArray(LV6gradeInputs);
        ArrayList<Double> L6Credits = getArray(LV6gradeInputs);
        GradeCalc gradeCalc = new GradeCalc(L5Grades, L5Credits, L6Grades, L6Credits);
        double methodA = gradeCalc.methodACalc();
        double methodB = gradeCalc.methodCCalc();

        methodAText.setText("Method A: "+methodA);
        methodBText.setText("Method B: "+methodB);

    }

    private ArrayList<Double> getArray(EditText[] array) {
        ArrayList<Double> newArray = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            String arrayText = array[i].getText().toString();
            if (!arrayText.isEmpty()) {
                try {
                    double doubleText = Double.parseDouble(arrayText);

                    newArray.add(doubleText);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Invalid input in row " + (i + 1), Toast.LENGTH_SHORT).show();
                    return null;
                }

            }
        }
        return newArray;
    }
}