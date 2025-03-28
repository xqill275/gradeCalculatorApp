package com.example.gradecalculatorapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Modules {
    @PrimaryKey(autoGenerate = true)
    public int moduleID;  // Fixed capitalization

    @ColumnInfo(name = "CreatedUserID")
    public int createdUserID;

    @ColumnInfo(name = "Title")
    public String title;

    @ColumnInfo(name = "Description")
    public String description;

    @ColumnInfo(name = "TargetGrade")
    public String targetGrade;

    @ColumnInfo(name = "CurrentGrade")
    public String currentGrade;


    public Modules(int createdUserID, String title, String description, String targetGrade, String currentGrade) {
        this.createdUserID = createdUserID;
        this.title = title;
        this.description = description;
        this.targetGrade = targetGrade;
        this.currentGrade = currentGrade;
    }
}
