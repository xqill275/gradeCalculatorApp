package com.example.gradecalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModualTrackerActivity extends AppCompatActivity {
    private static final int REFRESH_INTERVAL = 5000; // Refresh every 5 seconds

    Button addModualButton;
    int userID;
    private AppDatabase db;
    private ModulesDao ModulesDao;
    List<Modules> usersModules;
    private RecyclerView recyclerView;
    private ModulesAdapter adapter;
    private Handler handler = new Handler();
    private Runnable refreshRunnable;

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

        // Check if usersModules is null and initialize it if necessary
        if (usersModules == null) {
            usersModules = new ArrayList<>(); // Prevent NullPointerException
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.modulesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pass the data to the adapter
        adapter = new ModulesAdapter(this, usersModules, ModulesDao);
        recyclerView.setAdapter(adapter);

        addModualButton = findViewById(R.id.addModualButton);
        addModualButton.setOnClickListener(view -> gotoModualForm(userID));

        Button goBack = findViewById(R.id.GoBackButton);
        goBack.setOnClickListener(view -> finish());
        // Start auto-refreshing
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                refreshModules(); // Fetch new data and update UI
                handler.postDelayed(this, REFRESH_INTERVAL); // Schedule next refresh
            }
        };
        handler.post(refreshRunnable);
    }

    private void refreshModules() {
        usersModules = ModulesDao.getModulesByUser(userID);
        Log.d("Modual Tracker", "Updated Modules: " + usersModules.toString());

        runOnUiThread(() -> {
            adapter.updateModules(usersModules); // Ensure adapter has an update method
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshRunnable); // Stop refreshing when activity is destroyed
    }

    public void gotoModualForm(int userID) {
        Intent intent = new Intent(ModualTrackerActivity.this, AddModualForm.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }
}
