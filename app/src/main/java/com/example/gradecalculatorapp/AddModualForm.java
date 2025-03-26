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

public class AddModualForm extends AppCompatActivity {
    Button CancelButton, CreateButton;
    Spinner TargetGrade, CurrentGrade;
    EditText ModualTitle, ModualDescription;
    int UserID;
    private AppDatabase db;
    private ModulesDao ModulesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_modual_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);
        ModulesDao = db.modulesDao();

        UserID = getIntent().getIntExtra("userID", -1);
        ModualTitle = findViewById(R.id.ModualTItle);
        ModualDescription = findViewById(R.id.ModualDescription);
        TargetGrade = findViewById(R.id.TargetGrade);
        CurrentGrade = findViewById(R.id.CurrentGrade);
        CreateButton = findViewById(R.id.CreateModualButton);
        CancelButton = findViewById(R.id.CancelButton);

        CreateButton.setOnClickListener(view -> CreateModual());
        CancelButton.setOnClickListener(view -> goBack(UserID));
    }

    public void goBack(int UserID){
        Intent intent = new Intent(AddModualForm.this, ModualTrackerActivity.class);
        intent.putExtra("userID", UserID);
        startActivity(intent);
    }

    public void CreateModual() {
        String Title = ModualTitle.getText().toString().trim();
        String Description = ModualDescription.getText().toString().trim();
        String Target = TargetGrade.getSelectedItem().toString();
        String Current = CurrentGrade.getSelectedItem().toString();

        if (Title.isEmpty() || Description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log values for debugging
        Log.d("CreateModual", "Title: " + Title);
        Log.d("CreateModual", "Description: " + Description);
        Log.d("CreateModual", "Target Grade: " + Target);
        Log.d("CreateModual", "Current Grade: " + Current);

        Modules newModule = new Modules(UserID, Title, Description, Target, Current);

        try {
            ModulesDao.insertModule(newModule);
            Toast.makeText(this, "Module created successfully!", Toast.LENGTH_SHORT).show();
            finish();  // ✅ Close activity after success
        } catch (Exception e) {
            Log.e("CreateModual", "Error inserting module", e);
            Toast.makeText(this, "Failed to create module!", Toast.LENGTH_SHORT).show();
        }
    }
}
