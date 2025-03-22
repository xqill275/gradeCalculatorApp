package com.example.gradecalculatorapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.gradecalculatorapp.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertAll(User... users);  // Insert multiple users

    @Query("SELECT * FROM User")  // Fetch all users
    List<User> getAllUsers();
}