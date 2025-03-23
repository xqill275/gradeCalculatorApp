package com.example.gradecalculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private AppDatabase db;
    private UserDao userDao;
    private static final String TAG = "LoginActivity";
    Button LoginButton;
    EditText PasswordEdit, UsernameEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        LoginButton = findViewById(R.id.LoginActivityLoginButton);
        PasswordEdit = findViewById(R.id.editTextTextPassword3);
        UsernameEdit = findViewById(R.id.editTextText2);

        LoginButton.setOnClickListener(view -> handleLogin());
    }

    private void handleLogin(){
        String userNameText = UsernameEdit.getText().toString().trim();
        String passwordText = PasswordEdit.getText().toString();
        StringBuilder errorMsg = new StringBuilder();

        if (userNameText.isEmpty() || passwordText.isEmpty()){
            errorMsg.append("All fields must be filled!\n");
        }

        if (errorMsg.length() > 0){
            displayError(errorMsg.toString().trim());
        } else {
            attemptLogin(userNameText, passwordText);
        }
    }

    public void attemptLogin(String userName, String password) {
        if (userIsReal(userName)) {
            int userIndex = getUserIndex(userName);

            if (userIndex != -1) {  // Ensure the user exists
                String storedPassword = userDao.getPassword(userIndex);

                if (storedPassword != null && storedPassword.equals(password)) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    // Navigate to next activity or dashboard
                } else {
                    displayError("Incorrect password!");
                }
            } else {
                displayError("User not found!");
            }
        } else {
            displayError("User does not exist!");
        }
    }


    private boolean userIsReal(String userName){
        boolean foundUser = false;
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            if (user.username.equals(userName)){
                foundUser = true;
                break;
            }
        }
        return foundUser;
    }

    private int getUserIndex(String userName){
        Integer userIndex = userDao.getUserIndex(userName);
        return (userIndex != null) ? userIndex : -1;
    }

    private void displayError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }




}