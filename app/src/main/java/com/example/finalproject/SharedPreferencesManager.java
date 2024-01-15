package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";

    private static SharedPreferencesManager instance;
    private final SharedPreferences sharedPreferences;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveUserInfo(String firstName, String lastName, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public String getFirstName() {
        return sharedPreferences.getString(KEY_FIRST_NAME, "");
    }

    public String getLastName() {
        return sharedPreferences.getString(KEY_LAST_NAME, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public void saveEmail(String s) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, s);
        editor.apply();
    }

    public void setSignedIn(boolean b) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("signedIn", b);
        editor.apply();
    }

    public boolean getSignedIn() {
        return sharedPreferences.getBoolean("signedIn", false);
    }
}
