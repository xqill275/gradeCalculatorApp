package com.example.gradecalculatorapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class SignUpActivity extends AppCompatActivity {
    private AppDatabase db;
    private UserDao userDao;
    private static final String TAG = "SignUpActivity";

    private EditText Email, Username, Password, ConfirmPassword;
    private Button SignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);
        userDao = db.userDao();

        userDao.getAllUsers();

        Email = findViewById(R.id.SignUpEmaIl);
        Username = findViewById(R.id.editTextText);
        Password = findViewById(R.id.editTextTextPassword);
        ConfirmPassword = findViewById(R.id.editTextTextPassword2);
        SignUpButton = findViewById(R.id.button);
        Button goBack = findViewById(R.id.GoBackButton);
        goBack.setOnClickListener(view -> finish());

        SignUpButton.setOnClickListener(view -> handleSignUp());


    }

    private void handleSignUp() {
        String emailText = Email.getText().toString().trim();
        String userNameText = Username.getText().toString().trim();
        String passwordText = Password.getText().toString();
        String confirmText = ConfirmPassword.getText().toString();

        StringBuilder errorMsg = new StringBuilder();

        if (emailText.isEmpty() || userNameText.isEmpty() || passwordText.isEmpty() || confirmText.isEmpty()) {
            errorMsg.append("All fields must be filled!\n");
        }

        if (!passwordText.equals(confirmText)) {
            errorMsg.append("Passwords don't match!\n");
        }

        if (errorMsg.length() > 0) {
            displayError(errorMsg.toString().trim());
            return;
        }

        // Run database operations in the background
        new CheckAndRegisterUserTask(userNameText, emailText, passwordText).execute();
    }

    private void displayError(String errorText) {
        Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
    }

    private class CheckAndRegisterUserTask extends AsyncTask<Void, Void, Boolean> {
        private final String email, username, password;
        private String errorMessage = "";

        CheckAndRegisterUserTask(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            List<User> users = userDao.getAllUsers();

            for (User user : users) {
                if (user.username.equals(username)) {
                    errorMessage = "Username is already taken!";
                    return false;
                }
            }

            // Insert new user
            userDao.insertAll(new User(email, username, password));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                displayError("User successfully created!");
            } else {
                displayError(errorMessage);
            }
        }
    }
}
