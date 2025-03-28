# Oliver Long - Further Software Development - ASSESSMENT 2


### GitHub Link
```
https://github.com/xqill275/gradeCalculatorApp
```

### Data base 
My app contains one local database with two tables:

- Users – Stores user details.

- Modules – Stores information about different modules linked to users.

Why I Chose a Local Database

Initially, I attempted to use XAMPP and phpMyAdmin as I had in previous assessments. However, Android does not allow HTTP connections by default, which led me to store the database within the app itself.
There are both Pros & Cons to the use of a Local Database:

Pros:

- Faster Access – Queries run locally without requiring network calls.

- Easier Updates – No need for an external server, making database changes simpler.

Cons:

- Data Isolated Per Device – Since the database is stored locally, different phones running the same app will have separate versions of the database.

- No Data Sync – Sharing or syncing the database between devices is impossible with my current implementation.

#### Database Implementation
The database is created using Android Room, which provides an SQLite Database. Below is the main AppDatabase class that sets up the database:
```java
package com.example.gradecalculatorapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Modules.class}, version = 3)  
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ModulesDao modulesDao();  

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "user-database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE User ADD COLUMN ProfilePic TEXT DEFAULT ''");
        }
    };


    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS Modules (" +
                    "moduleID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "CreatedUserID INTEGER, " +
                    "Title TEXT, " +
                    "Description TEXT, " +
                    "TargetGrade TEXT, " +
                    "CurrentGrade TEXT, " +
                    "FOREIGN KEY (CreatedUserID) REFERENCES User(uid) ON DELETE CASCADE)");
        }
    };
}
```
#### key features of this implentation

- Singleton Pattern – This Ensures only one copy of the database exists throughout the app, this is so that when the app is relaunched it dosen't recreate a new database if it dosen't need to
- Thread Safety – Uses synchronised locking in getInstance() to ensure only one thread accesses the database at a time.
- Migrations – Preserves user data when upgrading the database by adding new columns and tables rather than replacing the entire database.




#### Why I Use allowMainThreadQueries()

In my app, I use allowMainThreadQueries() to allow me to accsess and edit the database on the main thread. While this is generally not recommended as it can cause potential performance issues as it may cause the ui thread to freeze while it completes the sql command, I chose this approach for the following reasons:

- Simplicity: My app is small, and database queries are lightweight, so the risk of UI freezing is minimal.
- Faster Development: Using this method allowed me to quickly implement and test database features without dealing with background threading.
- No Large Data Sets: Since my app doesn't handle large amounts of data, queries are fast enough to avoid noticeable lag.


#### DAO (Data Accsess Objects): 
I use two DAOs (one for users and one for modules) to handle database interactions. These DAOs primarily perform basic CRUD (Create, Read, Update, Delete) operations. Below is an example of the ModulesDao interface:

``` java
package com.example.gradecalculatorapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ModulesDao {
    @Insert
    void insertModule(Modules module);  

    @Query("SELECT * FROM Modules WHERE CreatedUserID = :userID")
    List<Modules> getModulesByUser(int userID);  
    
    @Query("DELETE FROM Modules WHERE moduleID = :moduleID")
    void deleteModule(int moduleID);  

    @Query("UPDATE Modules SET TargetGrade = :targetGrade WHERE moduleID = :moduleID")
    void updateTargetGrade(int moduleID, String targetGrade); 

    @Query("UPDATE Modules SET Title = :newTitle WHERE moduleID = :moduleID")
    void updateTitle(int moduleID, String newTitle); 

    @Query("UPDATE Modules SET Description = :newDescription WHERE moduleID = :moduleID")
    void updateDescription(int moduleID, String newDescription);  

    @Query("UPDATE Modules SET CurrentGrade = :newCurrentGrade WHERE moduleID = :moduleID")
    void updateCurrentGrade(int moduleID, String newCurrentGrade); 

}
```

### Login/SignUp

#### Sign-Up Screen

The sign-up screen allows users to create an account by entering their email, username, and password. The user input is validated before being stored in the database.

###### features

- User Input Validation:
    - Ensures all fields are filled.
    - Checks if the password and confirm password fields match.
    - Displays an error message if any validation check fails.
- Username Availability Check:
    - Before registering a new user, the app checks if the chosen username is already in use.
- Database Integration Using Room:
    - If validation passes, the new user is stored in the local database using the UserDao.insertAll() method.
- Async Task for Database Operations:
    -  The registration process runs in a background thread (AsyncTask) to prevent UI freezing.
    - After registration, the user receives a success or error message via a Toast notification.

###### Code Overview
1. UI Elements
    ``` java
    Email = findViewById(R.id.SignUpEmaIl);
    Username = findViewById(R.id.editTextText);
    Password = findViewById(R.id.editTextTextPassword);
    ConfirmPassword = findViewById(R.id.editTextTextPassword2);
    SignUpButton = findViewById(R.id.button);
    ```
    it first creates variables and finds all the input fields and buttonts on the xml file
2. Handling Sign-Up Button Click
    ``` java
    SignUpButton.setOnClickListener(view -> handleSignUp());
    ```
    When clicked, the handleSignUp() method validates user input and initiates the registration process.
3. Validating Input
    ``` java
    if (emailText.isEmpty() || userNameText.isEmpty() || passwordText.isEmpty() ||confirmText.isEmpty()) {
    errorMsg.append("All fields must be filled!\n");
    }

    if (!passwordText.equals(confirmText)) {
        errorMsg.append("Passwords don't match!\n");
    }

    ```
    if there is any errors in this step it will add the issue to the errorMsg variable.
4. Checking Username Availability & Registering the User
    ``` java
    new CheckAndRegisterUserTask(userNameText, emailText, passwordText).execute();
    ```
    This executes an AsyncTask to check if the username is taken and, if not, insert the new user into the database.
5. Background Registration Process
    ``` java
    @Override
    protected Boolean doInBackground(Void... voids) {
        List<User> users = userDao.getAllUsers();

        for (User user : users) {
            if (user.username.equals(username)) {
                errorMessage = "Username is already taken!";
                return false;
            }
        }

        userDao.insertAll(new User(email, username, password));
        return true;
    }

    ```
    This was the last edit I made of this project, I found a solution to allowMainThreadQueries() potentially cauasing the UI to freez, unfortunatly I ran out of time to implement it across the rest of the project
6. 
    ``` java
    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            displayError("User successfully created!");
        } else {
            displayError(errorMessage);
        }
    }
    ```

#### Login Screen
The login screen allows users to access their accounts by verifying their credentials against the local database.

###### Features & Functionality

- User Input Validation:

    - Ensures that the username and password fields are not empty.

    - Displays an error message if any field is left blank.

- User Authentication:

    - Checks if the entered username exists in the database.

    - Verifies the entered password against the stored password.

- Navigation to Dashboard:

    - If authentication is successful, the user is redirected to the dashboard with their userID passed as an intent extra.

###### Code Overview

The LoginActivity class handles user authentication.

1. Initializing UI Elements

``` java
LoginButton = findViewById(R.id.LoginActivityLoginButton);
goBack = findViewById(R.id.GoBackButton);
PasswordEdit = findViewById(R.id.editTextTextPassword3);
UsernameEdit = findViewById(R.id.editTextText2);
```

These elements allow users to enter their credentials and attempt login.

2. Handling Login Button Click

``` java

LoginButton.setOnClickListener(view -> handleLogin());
```

Clicking the login button triggers handleLogin(), which validates input and initiates authentication.

3. Validating User Input

``` java
if (userNameText.isEmpty() || passwordText.isEmpty()){
    errorMsg.append("All fields must be filled!\n");
}
```

If any field is empty, an error message is displayed.

4. Attempting Login

``` java
if (userIsReal(userName)) {
    int userIndex = getUserIndex(userName);

    if (userIndex != -1) {
        String storedPassword = userDao.getPassword(userIndex);

        if (storedPassword != null && storedPassword.equals(password)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            moveToDash(userIndex);
        } else {
            displayError("Incorrect password!");
        }
    } else {
        displayError("User not found!");
    }
} else {
    displayError("User does not exist!");
}
```

First, the app checks if the username exists using userIsReal().

Then, it retrieves the corresponding password from the database.

If the entered password matches the stored password, the user is successfully logged in. Otherwise, an error message is displayed.

5. Checking If a User Exists
``` java
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
```

This method iterates through all stored users to check if the username exists in the database.

6. Retrieving User ID

``` java
private int getUserIndex(String userName){
    Integer userIndex = userDao.getUserIndex(userName);
    return (userIndex != null) ? userIndex : -1;
}
```

This method fetches the unique user ID associated with the username. If the user is not found, it returns -1.

7. Navigating to the Dashboard

``` java

private void moveToDash(int userIndex){
    Intent Dashintent = new Intent(LoginActivity.this, DashBoard.class);
    Dashintent.putExtra("userID", userIndex);
    startActivity(Dashintent);
}
```


If the login is successful, the app navigates to the dashboard, passing the user’s ID as an extra in the intent.

### Dashboard Screen

The dashboard serves as the main hub of my app, it provides access to all the features of the app such as profile management, grade calculation, and module tracking.
Features & Functionality

- Profile Picture Management

    - Displays the user’s profile picture (default if none is set).

    - Allows users to change their profile picture by selecting an image from the gallery.

- Navigation to Key Features

    - Grade Calculator: Redirects to the grade calculator screen.

    - Module Tracker: Redirects to the module tracker screen.

- User Session Handling

    - The logged-in user’s ID (userID) is passed from the login screen, ensuring personalized access.

###### Code Overview
1. UI Initialization & User ID Handling

``` java
userID = getIntent().getIntExtra("userID", -1);
Log.d(TAG, "Received userID: " + userID);
```

Retrieves the logged-in user's ID from the intent.

If the ID is valid, the profile picture is displayed using displayProfilePic(userID).

2. Displaying Profile Picture
``` java
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
```

Retrieves the stored profile picture path from the database.

Uses Glide to load the image if it exists, otherwise, it sets a default profile image.

3. Changing Profile Picture

``` java
profilePicChange.setOnClickListener(view -> {
    if (checkStoragePermissions()) {
        openImageChooser();
    }
});
```

When the user clicks on "Change Profile Picture", it checks if storage permissions are granted before opening the image picker.

4. Handling Image Selection
``` java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
        Uri selectedImageUri = data.getData();
        if (selectedImageUri != null) {
            String savedImagePath = saveImageLocally(selectedImageUri);
            if (savedImagePath != null) {
                userDao.updateProfilePic(userID, savedImagePath);
                displayProfilePic(userID);
                Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error saving image!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
```

Handles image selection from the gallery and saves it locally.

Updates the profile picture path in the database and refreshes the UI.

5. Saving Image Locally
``` java
private String saveImageLocally(Uri uri) {
    File file = new File(getExternalFilesDir(null), "profile_pic.jpg");
    try (InputStream inputStream = getContentResolver().openInputStream(uri);
         OutputStream outputStream = new FileOutputStream(file)) {

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        return file.getAbsolutePath();
    } catch (IOException e) {
        Log.e(TAG, "Failed to save image locally", e);
    }
    return null;
}
```

Copies the selected image from external storage to a local file for persistent access.

6. Navigation to Other Features
``` java
gradeCalcButton.setOnClickListener(view -> gotoGradeCalc(userID));
modualTrackerButton.setOnClickListener(view -> gotoModualTracker(userID));
```

Navigates to the Grade Calculator and Module Tracker, passing the userID for personalized access.

7. Handling Storage Permissions
``` java
private boolean checkStoragePermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
```

Requests storage permission if required, depending on the Android version.

### Module Tracker Screen

The ModualTrackerActivity is responsible for displaying and managing a list of modules associated with a specific user. It retrieves the user's modules from the database and displays them in a RecyclerView, allowing users to track their progress. The activity also supports refreshing the module list automatically every 5 seconds.

###### Features & Functionality

Key Features

- Retrieves User's Modules from the database using Room.

- Displays Modules in a RecyclerView with an Adapter for easy UI updates.

- Auto-Refresh Mechanism that updates the list every 5 seconds.

- Navigation to Add Module Form for adding new modules.

- andles Null Cases Gracefully by initializing an empty list when necessary.

###### Code Overview

1. Retrieving Modules from the Database

``` java
    // Initialize database & DAO
    db = AppDatabase.getInstance(this);
    ModulesDao = db.modulesDao();

    // Fetch modules for this user
    usersModules = ModulesDao.getModulesByUser(userID);

    // Check if usersModules is null and initialize it if necessary
    if (usersModules == null) {
        usersModules = new ArrayList<>(); // Prevent NullPointerException
    }
```

The database instance is retrieved using AppDatabase.getInstance(this).

The ModulesDao is used to fetch all modules associated with the given userID.

A null check is performed to ensure usersModules is always initialized, avoiding NullPointerException.

2. Setting Up RecyclerView
``` java
    // Set up RecyclerView
    recyclerView = findViewById(R.id.modulesRecyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Pass the data to the adapter
    adapter = new ModulesAdapter(this, usersModules, ModulesDao);
    recyclerView.setAdapter(adapter);
```

A RecyclerView is used to display the list of modules.

LinearLayoutManager arranges the items in a vertical list.

ModulesAdapter handles displaying individual module items.

3. Implementing Auto-Refresh
``` java
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
```

A Handler is used to periodically update the module list every 5 seconds (REFRESH_INTERVAL = 5000).

This ensures that if new modules are added, the UI is updated automatically.

Refreshing the Module List
``` java
private void refreshModules() {
    usersModules = ModulesDao.getModulesByUser(userID);
    Log.d("Modual Tracker", "Updated Modules: " + usersModules.toString());

    runOnUiThread(() -> {
        adapter.updateModules(usersModules); // Ensure adapter has an update method
    });
}
```

Fetches the latest modules from the database.

Logs the updated module list for debugging.

Calls adapter.updateModules(usersModules) to update the UI on the main thread.

4. Navigation to Add Module Form

``` java
addModualButton = findViewById(R.id.addModualButton);
addModualButton.setOnClickListener(view -> gotoModualForm(userID));

public void gotoModualForm(int userID) {
    Intent intent = new Intent(ModualTrackerActivity.this, AddModualForm.class);
    intent.putExtra("userID", userID);
    startActivity(intent);
}
```

The "Add Module" button opens AddModualForm and passes the userID via an intent.

### Grade Calculation Logic

The GradeCalc class handles the calculation of weighted grade averages based on Level 5 and Level 6 grades and credits. It provides different calculation methods based on various weighting strategies.

Features & Functionality
Key Features
- Stores Level 5 and Level 6 grades and credits separately.

- Calculates weighted averages using different methods.

- Handles invalid input cases gracefully.

- Provides utility to convert EditText input into a numerical list.

#### Class Overview
1. Data Storage & Initialization

The class uses ArrayList<Double> to store grades and credits for both Level 5 and Level 6 modules.
``` java
private ArrayList<Double> LV5Grades;
private ArrayList<Double> LV6Grades;
private ArrayList<Double> LV5Credits;
private ArrayList<Double> LV6Credits;
```

These lists are initialized in the constructor:

``` java
public GradeCalc() {
    this.LV5Grades = new ArrayList<>();
    this.LV6Grades = new ArrayList<>();
    this.LV5Credits = new ArrayList<>();
    this.LV6Credits = new ArrayList<>();
}
```

2. Setting Grades & Credits

The class provides setter methods to update the stored values.

``` java

public void setLV5Grades(ArrayList<Double> newLV5Grades) {
    this.LV5Grades = newLV5Grades;
}

public void setLV6Grades(ArrayList<Double> newLV6Grades) {
    this.LV6Grades = newLV6Grades;
}

public void setLV5Credits(ArrayList<Double> newLV5Credits) {
    this.LV5Credits = newLV5Credits;
}

public void setLV6Credits(ArrayList<Double> newLV6Credits) {
    this.LV6Credits = newLV6Credits;
}

```
3. Grade Calculation Methods

The class includes three methods for calculating weighted grade averages.
Method C (Level 6 Weighted Average)

``` java
public double methodCCalc() {
    Log.d("GradeCalc", "GradeCalc: lv6 grades: " + LV6Grades + " lv6 credits: " + LV6Credits);
    return getWeightedAverage(LV6Grades, LV6Credits);
}
```

Computes the weighted average using only Level 6 grades and credits.

Logs the grades and credits for debugging.

Method A (Equal Contribution from Level 5 & Level 6)
``` java
public double methodACalc() {
    double LV5average = getWeightedAverage(LV5Grades, LV5Credits);
    double LV6average = getWeightedAverage(LV6Grades, LV6Credits);
    return (LV5average + LV6average) / 2;
}
```

Takes the average of Level 5 and Level 6 weighted grades.

Assumes equal contribution from both levels.

Method B (Higher Weight for Level 6)

``` java
public double methodBCalc() {
    double LV5average = getWeightedAverage(LV5Grades, LV5Credits);
    double LV6average = getWeightedAverage(LV6Grades, LV6Credits);
    return (LV5average + LV6average + LV6average) / 3;
}
```

Similar to Method A but gives Level 6 a higher weight.

Uses (LV5average + 2 * LV6average) / 3 to emphasize Level 6 performance.
``` java
private double getWeightedAverage(ArrayList<Double> inputGrades, ArrayList<Double> inputCredits) {
    double weightedSum = 0;
    double totalCredits = 0;

    for (int i = 0; i < inputGrades.size(); i++) {
        weightedSum += inputGrades.get(i) * inputCredits.get(i);
        totalCredits += inputCredits.get(i);
    }

    if (totalCredits == 0) return 0; // Prevent division by zero
    return weightedSum / totalCredits; // Weighted average formula
}
```

Multiplies each grade by its corresponding credit.

Sums up the weighted values and divides by total credits.

Prevents division by zero.

5. Extracting Numerical Data from UI Inputs

The getArray method converts an array of EditText fields into a list of Double values.
``` java
public ArrayList<Double> getArray(EditText[] array) {
    ArrayList<Double> newArray = new ArrayList<>();
    for (int i = 0; i < array.length; i++) {
        String arrayText = array[i].getText().toString();
        if (!arrayText.isEmpty()) {
            try {
                double doubleText = Double.parseDouble(arrayText);
                newArray.add(doubleText);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
    return newArray;
}
```

Iterates through an array of EditText fields.

Converts non-empty values to Double and adds them to the list.

Returns null if an invalid number is encountered.





