package com.example.gradecalculatorapp;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class DashBoard extends AppCompatActivity {
    private AppDatabase db;
    private UserDao userDao;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dash_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        profileImageView = findViewById(R.id.profileImageView);
        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        int userID = getIntent().getIntExtra("userID", -1);
        if (userID != -1) {
            displayProfilePic(userID);
        }
    }

    private void displayProfilePic(int userID) {
        // Get the profile picture location from the database
        String profilePicPath = userDao.getProfilePic(userID);

        if (profilePicPath != null && !profilePicPath.isEmpty()) {
            // Use Glide to load the image
            Glide.with(this)
                    .load(profilePicPath)
                    .into(profileImageView);
        } else {
            // Load default profile picture
            profileImageView.setImageResource(R.mipmap.ic_default_profile_pic_round);
        }
    }
}