package com.example.gradecalculatorapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Modules.class}, version = 3)  // ✅ Add Modules.class, Update version
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ModulesDao modulesDao();  // ✅ Add Modules DAO

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "user-database")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3) // ✅ Added MIGRATION_2_3
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ✅ Migration from v1 → v2 (User table update)
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE User ADD COLUMN ProfilePic TEXT DEFAULT ''");
        }
    };

    // ✅ Migration from v2 → v3 (Adding Modules table)
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


