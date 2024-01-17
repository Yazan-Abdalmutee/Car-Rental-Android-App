package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name

    // Database Information
    static final String DB_NAME = "CAPITALCARS.DB";

    // database version
    static final int DB_VERSION = 101;
    public static final String CAR_TABLE = "CAR";
    public static final String CUSTOMER_TABLE = "CUSTOMER";
    public static final String RESERVATION_TABLE = "RESERVATION";
    public static final String FAVORITE_TABLE = "FAVORITE";

    // Car Table columns
    public static final String CAR_ID = "carID";
    public static final String CAR_MAKE = "carMake";
    public static final String CAR_DRIVE = "carDrive";
    public static final String CAR_MODEL = "carModel";
    public static final String CAR_YEAR = "carYear";
    public static final String CAR_PRICE = "carPrice";
    public static final String CAR_VCLASS = "carVClass";
    public static final String CAR_FUEL = "carFuel";

    // Customer Table columns
    public static final String CUSTOMER_EMAIL = "email";
    public static final String CUSTOMER_FIRST_NAME = "firstName";
    public static final String CUSTOMER_LAST_NAME = "lastName";
    public static final String CUSTOMER_PASSWORD_HASHED = "passwordHashed";
    public static final String CUSTOMER_PHONE_NUMBER = "phoneNumber";
    public static final String CUSTOMER_COUNTRY = "country";
    public static final String CUSTOMER_CITY = "city";
    public static final String IS_ADMIN = "isAdmin";
    public static final String CUSTOMER_IMAGE = "image";

    // Reservation Table columns
    public static final String RESERVATION_ID = "reservationID";
    public static final String RESERVATION_CAR_ID = "carID";
    public static final String RESERVATION_EMAIL = "email";
    public static final String RESERVATION_DATE = "reservationDate";

    // Favorite Table columns
    public static final String FAVORITE_CAR_ID = "carID";
    public static final String FAVORITE_EMAIL = "email";
    public static final String FAVORITE_ID = "favoriteID";


    public static final String CUSTOMER_GENDER = "customerGender";
    public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE if not exists " + CUSTOMER_TABLE + "(" +
            CUSTOMER_EMAIL + " TEXT PRIMARY KEY, " +
            CUSTOMER_FIRST_NAME + " TEXT NOT NULL, " +
            CUSTOMER_LAST_NAME + " TEXT NOT NULL, " +
            CUSTOMER_PASSWORD_HASHED + " TEXT NOT NULL, " +
            CUSTOMER_PHONE_NUMBER + " TEXT NOT NULL, " +
            CUSTOMER_COUNTRY + " TEXT NOT NULL, " +
            CUSTOMER_CITY + " TEXT NOT NULL, " +
            CUSTOMER_GENDER + " TEXT NOT NULL, " +
            IS_ADMIN + " INTEGER NOT NULL);";
    public static final String CREATE_CAR_TABLE = "CREATE TABLE if not exists " + CAR_TABLE + "(" +
            CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAR_MAKE + " TEXT NOT NULL, " +
            CAR_MODEL + " TEXT NOT NULL, " +
            CAR_YEAR + " INTEGER NOT NULL, " +
            CAR_PRICE + " INTEGER NOT NULL, " +
            CAR_VCLASS + " TEXT NOT NULL, " +
            CAR_DRIVE + " TEXT NOT NULL, " +
            CAR_FUEL + " TEXT NOT NULL);";

    public static final String CREATE_RESERVATION_TABLE = "CREATE TABLE if not exists " + RESERVATION_TABLE + "(" +
            RESERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            RESERVATION_CAR_ID + " INTEGER NOT NULL, " +
            RESERVATION_EMAIL + " TEXT NOT NULL, " +
            RESERVATION_DATE + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + RESERVATION_CAR_ID + ") REFERENCES " + CAR_TABLE + "(" + CAR_ID + "), " +
            "FOREIGN KEY(" + RESERVATION_EMAIL + ") REFERENCES " + CUSTOMER_TABLE + "(" + CUSTOMER_EMAIL + "));";

    public static final String CREATE_FAVORITE_TABLE = "CREATE TABLE if not exists " + FAVORITE_TABLE + "(" +
            FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FAVORITE_CAR_ID + " INTEGER NOT NULL, " +
            FAVORITE_EMAIL + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + FAVORITE_CAR_ID + ") REFERENCES " + CAR_TABLE + "(" + CAR_ID + "), " +
            "FOREIGN KEY(" + FAVORITE_EMAIL + ") REFERENCES " + CUSTOMER_TABLE + "(" + CUSTOMER_EMAIL + "));";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_CAR_TABLE);
        db.execSQL(CREATE_RESERVATION_TABLE);
        db.execSQL(CREATE_FAVORITE_TABLE);
        // add an admin account with email admin@capital.cars and password admin
        String hashedPassword = PasswordHasher.hashPassword("admin");
        String sql = "INSERT INTO " + DatabaseHelper.CUSTOMER_TABLE +
                " VALUES ('admin@capital.cars', 'Mohammad', 'Abu-Shelbaia', " +
                "'" + hashedPassword + "', '0599999999', 'PS', 'Jerusalem', 'Male', 1)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion , int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CAR_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RESERVATION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE);
        onCreate(db);
    }
}