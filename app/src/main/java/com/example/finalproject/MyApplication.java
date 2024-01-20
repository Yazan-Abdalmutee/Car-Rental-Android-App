package com.example.finalproject;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.finalproject.DataBase.DatabaseManager;

public class MyApplication extends Application {

    private static DatabaseManager databaseManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize and open the database
        databaseManager = new DatabaseManager(this);
        databaseManager.open();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        // Close the database when the application is about to be terminated
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}