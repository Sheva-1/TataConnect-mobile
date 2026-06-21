package com.example.tataconnect;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "tata_connect_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Note: For production, use background threads
                    .build();
        }
        return instance;
    }
}