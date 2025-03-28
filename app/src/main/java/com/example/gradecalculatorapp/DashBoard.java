package com.example.gradecalculatorapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DashBoard extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_REQUEST = 100;
    private static final String TAG = "DashBoardActivity";  // Debugging tag

    private AppDatabase db;
    private UserDao userDao;
    private ImageView profileImageView;
    private TextView profilePicChange;
    private Button gradeCalcButton, modualTrackerButton;
    private int userID;

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
        profilePicChange = findViewById(R.id.textView3);
        gradeCalcButton = findViewById(R.id.gradeCalculatorButton);
        modualTrackerButton = findViewById(R.id.ModualTrackerButton);

        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        userID = getIntent().getIntExtra("userID", -1);
        Log.d(TAG, "Received userID: " + userID);

        if (userID != -1) {
            displayProfilePic(userID);
        } else {
            Log.e(TAG, "Invalid userID received!");
        }

        profilePicChange.setOnClickListener(view -> {
            if (checkStoragePermissions()) {
                openImageChooser();
            }
        });

        gradeCalcButton.setOnClickListener(view -> gotoGradeCalc(userID));
        modualTrackerButton.setOnClickListener(view -> gotoModualTracker(userID));

        Button goBack = findViewById(R.id.GoBackButton);
        goBack.setOnClickListener(view -> finish());
    }

    private void gotoGradeCalc(int userID){
        Intent intent = new Intent(DashBoard.this, GradeMenuActivty.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    public void gotoModualTracker(int userID) {
        Intent intent = new Intent(DashBoard.this, ModualTrackerActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

    private void displayProfilePic(int userID) {
        String profilePicUri = userDao.getProfilePic(userID);
        Log.d(TAG, "Profile picture URI retrieved: " + profilePicUri);

        if (profilePicUri == null || profilePicUri.isEmpty()) {
            Log.w(TAG, "No profile picture found, loading default image.");
            profileImageView.setImageResource(R.mipmap.ic_default_profile_pic_round);
        } else {
            File imgFile = new File(profilePicUri);
            if (imgFile.exists()) {
                Glide.with(this)
                        .load(imgFile)
                        .into(profileImageView);
                Log.d(TAG, "Profile picture displayed successfully.");
            } else {
                Log.e(TAG, "Profile picture path is set but file does not exist! Using default.");
                profileImageView.setImageResource(R.mipmap.ic_default_profile_pic_round);
            }
        }
    }

    private void openImageChooser() {
        Log.d(TAG, "Opening image chooser...");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult called. requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            Log.d(TAG, "Selected image URI: " + selectedImageUri);

            if (selectedImageUri != null) {
                String savedImagePath = saveImageLocally(selectedImageUri);
                if (savedImagePath != null) {
                    userDao.updateProfilePic(userID, savedImagePath);
                    Log.d(TAG, "Profile picture updated in database.");

                    displayProfilePic(userID);

                    Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Failed to save image.");
                    Toast.makeText(this, "Error saving image!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Log.w(TAG, "Image selection cancelled or failed.");
        }
    }

    /**
     * Saves the image locally .
     */
    private String saveImageLocally(Uri uri) {
        File file = new File(getExternalFilesDir(null), "profile_pic.jpg");
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            Log.d(TAG, "Image saved locally: " + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException e) {
            Log.e(TAG, "Failed to save image locally", e);
        }
        return null;
    }

    /**
     * Checks and Requests Storage Permissions for different Android versions.
     */
    private boolean checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, STORAGE_PERMISSION_REQUEST);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
                return false;
            }
        }
        return true;
    }

    /**
     * Handles Permission Request Results.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Storage permission granted.");
                openImageChooser();
            } else {
                Log.e(TAG, "Storage permission denied!");
                Toast.makeText(this, "Storage permission is required to change the profile picture!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
