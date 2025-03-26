package com.example.gradecalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ModualTrackerActivity extends AppCompatActivity {
    Button addModualButton;
    int userID;
    private AppDatabase db;
    private ModulesDao ModulesDao;
    List<Modules> usersModules;
    private RecyclerView recyclerView;
    private ModulesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modual_tracker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userID = getIntent().getIntExtra("userID", -1);

        // Initialize database & DAO
        db = AppDatabase.getInstance(this);
        ModulesDao = db.modulesDao();

        // Fetch modules for this user
        usersModules = ModulesDao.getModulesByUser(userID);
        Log.d("Modual Tracker", "User Modules: " + usersModules.toString());

        // Set up RecyclerView
        recyclerView = findViewById(R.id.modulesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ModulesAdapter(usersModules);
        recyclerView.setAdapter(adapter);

        addModualButton = findViewById(R.id.addModualButton);
        addModualButton.setOnClickListener(view -> gotoModualForm(userID));
    }

    public void gotoModualForm(int userID) {
        Intent intent = new Intent(ModualTrackerActivity.this, AddModualForm.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}
