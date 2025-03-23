package com.example.gradecalculatorapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 2)  // ⬆️ Updated version from 1 to 2
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    // Singleton to prevent multiple instances
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "user-database")
                            .addMigrations(MIGRATION_1_2) // Handle schema change
                            .allowMainThreadQueries() // Not recommended for large apps
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Migration from version 1 to 2 (Adding ProfilePic column)
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE User ADD COLUMN ProfilePic TEXT DEFAULT ''");
        }
    };
}
