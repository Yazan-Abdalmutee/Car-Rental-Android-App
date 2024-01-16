package com.example.finalproject;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_REMEMBER_ME = "remember_me";

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

    public void saveUserInfo(String email, String firstName, String lastName, String passwordHashed,
                             String phoneNumber, String country, String city, String gender, int isAdmin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(DatabaseHelper.CUSTOMER_PASSWORD_HASHED, passwordHashed);
        editor.putString(DatabaseHelper.CUSTOMER_PHONE_NUMBER, phoneNumber);
        editor.putString(DatabaseHelper.CUSTOMER_COUNTRY, country);
        editor.putString(DatabaseHelper.CUSTOMER_CITY, city);
        editor.putInt(DatabaseHelper.IS_ADMIN, isAdmin);
        editor.putString(DatabaseHelper.CUSTOMER_CITY, city);
        editor.putString(DatabaseHelper.CUSTOMER_GENDER, gender);
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
    public void clearAllButRememberMe() {
        String rememberMe = sharedPreferences.getString(KEY_REMEMBER_ME, "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    public void rememberMe(String s) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_REMEMBER_ME, s);
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

    public void setEmail(String string) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, string);
        editor.apply();
    }

    public String getRememberMe() {
        return sharedPreferences.getString(KEY_REMEMBER_ME, "");
    }

    public String getCountry() {
        return sharedPreferences.getString(DatabaseHelper.CUSTOMER_COUNTRY, "");

    }

    public CharSequence getPhone() {
        return sharedPreferences.getString(DatabaseHelper.CUSTOMER_PHONE_NUMBER, "");
    }

    public void setFirstName(String string) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, string);
        editor.apply();
    }

    public void setLastName(String string) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LAST_NAME, string);
        editor.apply();
    }

    public void setPhone(String string) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DatabaseHelper.CUSTOMER_PHONE_NUMBER, string);
        editor.apply();
    }

    public void setPasswordHashed(String passwordHashed) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DatabaseHelper.CUSTOMER_PASSWORD_HASHED, passwordHashed);
        editor.apply();
    }
}
