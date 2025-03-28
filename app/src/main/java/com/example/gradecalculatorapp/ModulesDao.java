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
