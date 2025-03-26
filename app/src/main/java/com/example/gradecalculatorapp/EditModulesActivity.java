package com.example.gradecalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditModulesActivity extends AppCompatActivity {
    Button CancelButton, CreateButton;
    Spinner TargetGrade, CurrentGrade;
    EditText ModualTitle, ModualDescription;
    int ModualID;
    private AppDatabase db;
    private ModulesDao ModulesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_modules);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);
        ModulesDao = db.modulesDao();

        ModualID = getIntent().getIntExtra("moduleID", -1);
        ModualTitle = findViewById(R.id.ModualTItle);
        ModualDescription = findViewById(R.id.ModualDescription);
        TargetGrade = findViewById(R.id.TargetGrade);
        CurrentGrade = findViewById(R.id.CurrentGrade);
        CreateButton = findViewById(R.id.CreateModualButton);
        CancelButton = findViewById(R.id.CancelButton);

        CreateButton.setOnClickListener(view -> EditModual());
        CancelButton.setOnClickListener(view -> finish());
    }

    public void goBack(int UserID){
        Intent intent = new Intent(EditModulesActivity.this, ModualTrackerActivity.class);
        intent.putExtra("userID", UserID);
        startActivity(intent);
    }

    public void EditModual() {
        String Title = ModualTitle.getText().toString().trim();
        String Description = ModualDescription.getText().toString().trim();
        String Target = TargetGrade.getSelectedItem().toString();
        String Current = CurrentGrade.getSelectedItem().toString();

        if (Title.isEmpty() || Description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log values for debugging
        Log.d("EditModual", "Title: " + Title);
        Log.d("EditModual", "Description: " + Description);
        Log.d("EditModual", "Target Grade: " + Target);
        Log.d("EditModual", "Current Grade: " + Current);


        try {
            ModulesDao.updateTitle(ModualID, Title);
            ModulesDao.updateDescription(ModualID, Description);
            ModulesDao.updateCurrentGrade(ModualID, Current);
            ModulesDao.updateTargetGrade(ModualID, Target);
            Toast.makeText(this, "Module Edited successfully!", Toast.LENGTH_SHORT).show();
            finish();  // ✅ Close activity after success
        } catch (Exception e) {
            Log.e("EditModual", "Error inserting module", e);
            Toast.makeText(this, "Failed to create module!", Toast.LENGTH_SHORT).show();
        }
    }
}