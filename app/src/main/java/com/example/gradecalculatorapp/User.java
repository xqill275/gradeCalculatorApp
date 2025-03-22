package com.example.gradecalculatorapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)  // Auto-increments ID
    public int uid;

    @ColumnInfo(name = "Email")
    public String email;

    @ColumnInfo(name = "Username")
    public String username;

    @ColumnInfo(name = "Password")
    public String password;

    // Constructor
    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }
}
