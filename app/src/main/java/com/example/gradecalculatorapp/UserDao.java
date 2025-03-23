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

    @Query("SELECT Password FROM User WHERE uid == :userIndex")
    String getPassword(int userIndex);  // Return a single password

    @Query("SELECT uid FROM User WHERE username = :userName LIMIT 1")
    Integer getUserIndex(String userName);

    @Query("SELECT ProfilePicLocation FROM User WHERE uid = :userID")
    String getProfilePic(int userID);
}