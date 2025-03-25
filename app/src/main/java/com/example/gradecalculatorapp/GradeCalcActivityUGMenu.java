package com.example.gradecalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GradeCalcActivityUGMenu extends AppCompatActivity {
    Button Lv5Lv6Button;
    Button LV6Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_grade_calc_ugmenu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int userID = getIntent().getIntExtra("UserID", -1);
        Lv5Lv6Button = findViewById(R.id.Lv5Lv6Button);
        LV6Button = findViewById(R.id.Lv6Button);

        Lv5Lv6Button.setOnClickListener(view -> goto5and6(userID));

    }

    private void goto5and6(int userId){
        Intent intent = new Intent(GradeCalcActivityUGMenu.this, LV5ANDLV6UGActivity.class);
        intent.putExtra("userID", userId);
        startActivity(intent);
    }

    private void goto6(int userID){
        Intent intent = new Intent(GradeCalcActivityUGMenu.this, LV6UGActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}