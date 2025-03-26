package com.example.gradecalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class GradeMenuActivty extends AppCompatActivity {
    Button FYButton;
    Button UGButton;
    Button PGButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grade_menu_activty);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FYButton = findViewById(R.id.FYButton);
        UGButton = findViewById(R.id.UGButton);
        PGButton = findViewById(R.id.PostGradButton);

        int userID = getIntent().getIntExtra("userID", -1);

        FYButton.setOnClickListener(view -> gotoFY(userID));
        UGButton.setOnClickListener(view -> gotoUG(userID));
        PGButton.setOnClickListener(view -> gotoPG(userID));

        Button goBack = findViewById(R.id.GoBackButton);
        goBack.setOnClickListener(view -> finish());
    }

    private void gotoFY(int userID){
        Intent intent = new Intent(GradeMenuActivty.this, GradeCalcActivityFY.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void gotoUG(int userID){
        Intent intent = new Intent(GradeMenuActivty.this, GradeCalcActivityUGMenu.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void gotoPG(int userID) {
        Intent intent = new Intent(GradeMenuActivty.this, GradeCalcActivityPGMenu.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}