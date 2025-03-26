package com.example.gradecalculatorapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity  // ✅ Mark it as an entity so Room recognizes it as a table
public class Modules {   // Or name it "Module" for better naming
    @PrimaryKey(autoGenerate = true)
    public int moduleID;  // ✅ Fixed capitalization

    @ColumnInfo(name = "CreatedUserID")
    public int createdUserID;  // ✅ Changed to int to match User ID type

    @ColumnInfo(name = "Title")
    public String title;  // ✅ Fixed capitalization

    @ColumnInfo(name = "Description")
    public String description;

    @ColumnInfo(name = "TargetGrade")
    public String targetGrade;  // ✅ Fixed capitalization

    @ColumnInfo(name = "CurrentGrade")
    public String currentGrade;

    // ✅ Constructor for easy object creation
    public Modules(int createdUserID, String title, String description, String targetGrade, String currentGrade) {
        this.createdUserID = createdUserID;
        this.title = title;
        this.description = description;
        this.targetGrade = targetGrade;
        this.currentGrade = currentGrade;
    }
}
