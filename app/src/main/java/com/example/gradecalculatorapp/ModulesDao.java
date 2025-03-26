package com.example.gradecalculatorapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ModulesDao {
    @Insert
    void insertModule(Modules module);  // ✅ Insert a new module

    @Query("SELECT * FROM Modules WHERE CreatedUserID = :userID")
    List<Modules> getModulesByUser(int userID);  // ✅ Get all modules for a specific user

    @Query("DELETE FROM Modules WHERE moduleID = :moduleID")
    void deleteModule(int moduleID);  // ✅ Delete a module by ID

    @Query("UPDATE Modules SET TargetGrade = :targetGrade WHERE moduleID = :moduleID")
    void updateTargetGrade(int moduleID, String targetGrade);  // ✅ Update module target grade
}
