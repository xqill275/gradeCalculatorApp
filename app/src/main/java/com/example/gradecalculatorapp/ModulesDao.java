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
    void updateTargetGrade(int moduleID, String targetGrade); // ✅ Update module target grade

    @Query("UPDATE Modules SET Title = :newTitle WHERE moduleID = :moduleID")
    void updateTitle(int moduleID, String newTitle); // ✅ Update module title

    @Query("UPDATE Modules SET Description = :newDescription WHERE moduleID = :moduleID")
    void updateDescription(int moduleID, String newDescription); // ✅ Update module description

    @Query("UPDATE Modules SET CurrentGrade = :newCurrentGrade WHERE moduleID = :moduleID")
    void updateCurrentGrade(int moduleID, String newCurrentGrade); // ✅ Update module current grade


    // Add more update methods if your Modules class has more fields
}
