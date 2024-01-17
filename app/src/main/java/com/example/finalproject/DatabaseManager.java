package com.example.finalproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {

    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert a new customer into the CUSTOMER_TABLE
    public void insertCustomer(String email, String firstName, String lastName, String passwordHashed,
                               String phoneNumber, String gender, String country, String city) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CUSTOMER_EMAIL, email);
        values.put(DatabaseHelper.CUSTOMER_FIRST_NAME, firstName);
        values.put(DatabaseHelper.CUSTOMER_LAST_NAME, lastName);
        values.put(DatabaseHelper.CUSTOMER_PASSWORD_HASHED, passwordHashed);
        values.put(DatabaseHelper.CUSTOMER_PHONE_NUMBER, phoneNumber);
        values.put(DatabaseHelper.CUSTOMER_COUNTRY, country);
        values.put(DatabaseHelper.CUSTOMER_CITY, city);
        values.put(DatabaseHelper.CUSTOMER_GENDER, gender);
        values.put(DatabaseHelper.IS_ADMIN, 0);

        database.insert(DatabaseHelper.CUSTOMER_TABLE, null, values);
    }

    // Insert a new car into the CAR_TABLE
    public void insertCar(String carName, String carDrive, String carModel, int carYear,
                          int carPrice, String carVClass, String carFuel) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CAR_MAKE, carName);
        values.put(DatabaseHelper.CAR_DRIVE, carDrive);
        values.put(DatabaseHelper.CAR_MODEL, carModel);
        values.put(DatabaseHelper.CAR_YEAR, carYear);
        values.put(DatabaseHelper.CAR_PRICE, carPrice);
        values.put(DatabaseHelper.CAR_VCLASS, carVClass);
        values.put(DatabaseHelper.CAR_FUEL, carFuel);

        database.insert(DatabaseHelper.CAR_TABLE, null, values);
    }

    // Insert a new reservation into the RESERVATION_TABLE
    public long insertReservation(int carId, String email, String reservationDate) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.RESERVATION_CAR_ID, carId);
        values.put(DatabaseHelper.RESERVATION_EMAIL, email);
        values.put(DatabaseHelper.RESERVATION_DATE, reservationDate);

        return database.insert(DatabaseHelper.RESERVATION_TABLE, null, values);
    }

    // Insert a new favorite into the FAVORITE_TABLE
    public long insertFavorite(int carId, String email) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FAVORITE_CAR_ID, carId);
        values.put(DatabaseHelper.FAVORITE_EMAIL, email);

        return database.insert(DatabaseHelper.FAVORITE_TABLE, null, values);
    }

    // Example query: Get all customers
    public Cursor getAllCustomers() {
        return database.query(DatabaseHelper.CUSTOMER_TABLE, null, null, null, null, null, null);
    }

    // Example query: Get all cars
    public Cursor getAllCars() {
        return database.query(DatabaseHelper.CAR_TABLE, null, null, null, null, null, null);
    }

    public Cursor getCustomerReservations(String email) {
        return database.query(DatabaseHelper.RESERVATION_TABLE, null, DatabaseHelper.RESERVATION_EMAIL + " = ?", new String[]{email}, null, null, null);
    }

    public Cursor getCustomerFavorites(String email) {
        return database.query(DatabaseHelper.FAVORITE_TABLE, null, DatabaseHelper.FAVORITE_EMAIL + " = ?", new String[]{email}, null, null, null);
    }

    public boolean isEmailUsed(String email) {

        try (Cursor cursor = database.query(
                DatabaseHelper.CUSTOMER_TABLE,
                null,
                DatabaseHelper.CUSTOMER_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        )) {

            // Check if the cursor has rows
            return cursor != null && cursor.moveToFirst();

        }
        // Close the cursor in a finally block to ensure it gets closed even if an exception occurs
    }

    public boolean isLoginCredentialsValid(String email, String password) {
        try (Cursor cursor = database.query(
                DatabaseHelper.CUSTOMER_TABLE,
                null,
                "LOWER(" + DatabaseHelper.CUSTOMER_EMAIL + ") = ? AND " + DatabaseHelper.CUSTOMER_PASSWORD_HASHED + " = ?",
                new String[]{email.toLowerCase(), password},
                null,
                null,
                null
        )) {
            // Check if the cursor has any rows, indicating a match
            return cursor != null && cursor.getCount() > 0;
        }
        // Close the cursor in a finally block to ensure it gets closed even if an exception occurs
    }

    public int getCarCount() {
        try (Cursor cursor = database.query(
                DatabaseHelper.CAR_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
        )) {
            return cursor != null ? cursor.getCount() : 0;
        }
    }


    public void clearAllTables() {

        String[] allTables = {"CAR", "CUSTOMER", "RESERVATION", "FAVORITE"};
        for (String tableName : allTables) {
            database.delete(tableName, null, null);
        }
    }

    public Cursor getCarsByFilter(String[] makes, String[] fuels, int minYear, int maxYear, int minPrice, int maxPrice) {

        StringBuilder whereClause = new StringBuilder();

        if (makes != null && makes.length > 0) {
            whereClause.append("(");
            for (int i = 0; i < makes.length; i++) {
                whereClause.append(DatabaseHelper.CAR_MAKE).append(" = '").append(makes[i]).append("'");
                if (i < makes.length - 1) {
                    whereClause.append(" OR ");
                }
            }
            whereClause.append(")");
        }

        if (fuels != null && fuels.length > 0) {
            if (makes != null && makes.length > 0)
                whereClause.append(" AND ");

            whereClause.append("(");
            for (int i = 0; i < fuels.length; i++) {
                whereClause.append(DatabaseHelper.CAR_FUEL).append(" = '").append(fuels[i]).append("'");
                if (i < fuels.length - 1) {
                    whereClause.append(" OR ");
                }
            }
            whereClause.append(")");

        }
        if ((makes != null && makes.length > 0) || (fuels != null && fuels.length > 0))
            whereClause.append(" AND ");

        whereClause.append("(")
                .append(DatabaseHelper.CAR_YEAR).append(" BETWEEN ").append(minYear).append(" AND ").append(maxYear)
                .append(") AND (")
                .append(DatabaseHelper.CAR_PRICE).append(" BETWEEN ").append(minPrice).append(" AND ").append(maxPrice)
                .append(")");

        Cursor cursor = database.query(DatabaseHelper.CAR_TABLE, null, whereClause.toString(), null, null, null, null);

        return cursor;
    }


    public Cursor customerInfo(String email) {
        Cursor cursor = null;

        try {
            cursor = database.query(
                    DatabaseHelper.CUSTOMER_TABLE,
                    null,
                    DatabaseHelper.CUSTOMER_EMAIL + " = ?",
                    new String[]{email},
                    null,
                    null,
                    null
            );

            // Note: If no matching record is found, cursor will be null
        } catch (Exception e) {
            // Handle any exceptions here
            e.printStackTrace();
        }

        return cursor;
    }


    // Helper method to safely retrieve string values
    private String getColumnValue(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex != -1) ? cursor.getString(columnIndex) : null;
    }

    // Helper method to safely retrieve integer values
    private int getColumnIntValue(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex != -1) ? cursor.getInt(columnIndex) : 0; // or another default value as needed
    }

    public void editCustomer(String email, String firstName, String lastName, String phone, String password) {
        // Assuming 'database' is your SQLiteDatabase instance

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CUSTOMER_FIRST_NAME, firstName);
        values.put(DatabaseHelper.CUSTOMER_LAST_NAME, lastName);
        values.put(DatabaseHelper.CUSTOMER_PHONE_NUMBER, phone);
        values.put(DatabaseHelper.CUSTOMER_PASSWORD_HASHED, password);

        // Specify the condition for the update
        String selection = DatabaseHelper.CUSTOMER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        // Perform the update operation
        database.update(
                DatabaseHelper.CUSTOMER_TABLE,
                values,
                selection,
                selectionArgs
        );


    }
}