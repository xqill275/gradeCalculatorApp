package com.example.gradecalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GradeCalcActivityPGMenu extends AppCompatActivity {
    Button MasterAwardButton, PostGradeDegreeButton, PostGradeCertificate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grade_calc_pg_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int userID = getIntent().getIntExtra("UserID", -1);
        MasterAwardButton = findViewById(R.id.PGMasterAwardButton);
        PostGradeDegreeButton = findViewById(R.id.PostGradDegreeButton);
        PostGradeCertificate = findViewById(R.id.PGPostGradCertificateButton);

        MasterAwardButton.setOnClickListener(view -> gotoPGAward(userID));
        PostGradeDegreeButton.setOnClickListener(view -> gotoPGDegree(userID));
        PostGradeCertificate.setOnClickListener(view -> gotoPGCertificate(userID));
    }

    public void gotoPGAward(int userID){
        Intent intent = new Intent(GradeCalcActivityPGMenu.this, GradeCalcActivityPGAward.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void gotoPGDegree(int userID){
        Intent intent = new Intent(GradeCalcActivityPGMenu.this, GradeCalcActivityPGDegree.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void gotoPGCertificate(int userID){
        Intent intent = new Intent(GradeCalcActivityPGMenu.this, GradeCalcActivityPGCertificate.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }


}