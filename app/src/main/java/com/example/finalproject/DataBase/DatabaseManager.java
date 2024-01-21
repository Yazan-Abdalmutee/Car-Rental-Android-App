package com.example.finalproject.DataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
                               String phoneNumber, String gender, String country, String city, int isAdmin) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CUSTOMER_EMAIL, email.toLowerCase());
        values.put(DatabaseHelper.CUSTOMER_FIRST_NAME, firstName);
        values.put(DatabaseHelper.CUSTOMER_LAST_NAME, lastName);
        values.put(DatabaseHelper.CUSTOMER_PASSWORD_HASHED, passwordHashed);
        values.put(DatabaseHelper.CUSTOMER_PHONE_NUMBER, phoneNumber);
        values.put(DatabaseHelper.CUSTOMER_COUNTRY, country);
        values.put(DatabaseHelper.CUSTOMER_CITY, city);
        values.put(DatabaseHelper.CUSTOMER_GENDER, gender);
        values.put(DatabaseHelper.IS_ADMIN, isAdmin);
        database.insert(DatabaseHelper.CUSTOMER_TABLE, null, values);
    }


    public void updateCustomerImage(String email, byte[] image) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CUSTOMER_IMAGE, image);
        String whereClause = DatabaseHelper.CUSTOMER_EMAIL + "=?";
        String[] whereArgs = {email.toLowerCase()};
        database.update(DatabaseHelper.CUSTOMER_TABLE, values, whereClause, whereArgs);
    }

    public void updateCarImage(int carId, byte[] image) {
        if (image == null || image.length == 0) {
            // Handle the case where the image is null or empty
            Log.d("updateCarImage", "Invalid image data provided for carId: " + carId);
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CAR_IMAGE, image);

        String selection = DatabaseHelper.CAR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(carId)};

        int rowsUpdated = database.update(DatabaseHelper.CAR_TABLE, values, selection, selectionArgs);

        if (rowsUpdated <= 0) {
            // Handle the case where the update operation didn't affect any rows
            Log.d("updateCarImage", "Failed to update image for carId: " + carId);
        }
    }



    // Insert a new car into the CAR_TABLE
    public void insertCar(String carName, String carDrive, String carModel, int carYear,
                          int carPrice, String carVClass, String carFuel, int carOffer,
                          byte[] carImage) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CAR_MAKE, carName);
        values.put(DatabaseHelper.CAR_DRIVE, carDrive);
        values.put(DatabaseHelper.CAR_MODEL, carModel);
        values.put(DatabaseHelper.CAR_YEAR, carYear);
        values.put(DatabaseHelper.CAR_PRICE, carPrice);
        values.put(DatabaseHelper.CAR_VCLASS, carVClass);
        values.put(DatabaseHelper.CAR_FUEL, carFuel);
        values.put(DatabaseHelper.CAR_OFFER, carOffer);
        values.put(DatabaseHelper.CAR_IMAGE, carImage);

        database.insert(DatabaseHelper.CAR_TABLE, null, values);
    }

    // Insert a new reservation into the RESERVATION_TABLE
    public long insertReservation(int carId, String email, String reservationDate) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.RESERVATION_CAR_ID, carId);
        values.put(DatabaseHelper.RESERVATION_EMAIL, email.toLowerCase());
        values.put(DatabaseHelper.RESERVATION_DATE, reservationDate);


        return database.insert(DatabaseHelper.RESERVATION_TABLE, null, values);
    }

    // Insert a new favorite into the FAVORITE_TABLE
    public long insertFavorite(int carId, String email) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.FAVORITE_CAR_ID, carId);
        values.put(DatabaseHelper.FAVORITE_EMAIL, email.toLowerCase());

        return database.insert(DatabaseHelper.FAVORITE_TABLE, null, values);
    }


    public int removeFavorite(int carId, String email) {
        String whereClause = DatabaseHelper.FAVORITE_CAR_ID + " = ? AND " + DatabaseHelper.FAVORITE_EMAIL + " = ?";
        String[] whereArgs = {String.valueOf(carId), email.toLowerCase()};

        return database.delete(DatabaseHelper.FAVORITE_TABLE, whereClause, whereArgs);
    }

    public boolean isCarInFavorites(int carId, String email) {
        String[] columns = {DatabaseHelper.FAVORITE_ID};
        String selection = DatabaseHelper.FAVORITE_CAR_ID + " = ? AND " + DatabaseHelper.FAVORITE_EMAIL + " = ?";
        String[] selectionArgs = {String.valueOf(carId), email.toLowerCase()};

        try (Cursor cursor = database.query(DatabaseHelper.FAVORITE_TABLE, columns, selection, selectionArgs, null, null, null)) {
            return cursor != null && cursor.moveToFirst();
        }
    }


    public Cursor getReservationsCarsForUser(String userEmail) {
        userEmail = userEmail.toLowerCase();
        String query = "SELECT * FROM " + DatabaseHelper.CAR_TABLE +
                " INNER JOIN " + DatabaseHelper.RESERVATION_TABLE +
                " ON " + DatabaseHelper.CAR_TABLE + "." + DatabaseHelper.CAR_ID +
                " = " + DatabaseHelper.RESERVATION_TABLE + "." + DatabaseHelper.RESERVATION_CAR_ID +
                " WHERE " + DatabaseHelper.RESERVATION_TABLE + "." + DatabaseHelper.RESERVATION_EMAIL +
                " = '" + userEmail + "'";
        return database.rawQuery(query, null);
    }

    public Cursor getCarsNotInReservation() {
        String carTable = DatabaseHelper.CAR_TABLE;
        String reservationTable = DatabaseHelper.RESERVATION_TABLE;

        // Subquery to select car IDs that are in reservations
        String subquery = "SELECT DISTINCT " + DatabaseHelper.RESERVATION_CAR_ID +
                " FROM " + reservationTable;

        // Main query to select cars not in reservations
        String query = "SELECT * FROM " + carTable +
                " WHERE " + DatabaseHelper.CAR_ID + " NOT IN (" + subquery + ")";

        return database.rawQuery(query, null);
    }


    public boolean isCarInReservation(int carId) {
        String selection = DatabaseHelper.RESERVATION_CAR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(carId)};

        Cursor cursor = database.query(
                DatabaseHelper.RESERVATION_TABLE,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isInReservation = cursor.moveToFirst();

        // Close the cursor to avoid memory leaks
        cursor.close();

        return isInReservation;
    }

    public String getReservationDateForCar(int carId) {
        String reservationDate = null;
        String query = "SELECT " + DatabaseHelper.RESERVATION_DATE +
                " FROM " + DatabaseHelper.RESERVATION_TABLE +
                " WHERE " + DatabaseHelper.RESERVATION_CAR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(carId)};
        Cursor cursor = database.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.RESERVATION_DATE);
            if (columnIndex != -1) {
                reservationDate = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        return reservationDate;
    }


    public Cursor getAllCustomers() {
        String selection = DatabaseHelper.IS_ADMIN + " = 0";
        return database.query(DatabaseHelper.CUSTOMER_TABLE, null, selection, null, null, null, null);
    }

    public Cursor getAllCars() {
        return database.query(DatabaseHelper.CAR_TABLE, null, null, null, null, null, null);
    }

    public Cursor getAllReserves() {
        String query = "SELECT " +
                DatabaseHelper.CUSTOMER_TABLE + "." + DatabaseHelper.CUSTOMER_FIRST_NAME + ", " +
                DatabaseHelper.CUSTOMER_TABLE + "." + DatabaseHelper.CUSTOMER_LAST_NAME + ", " +
                DatabaseHelper.CUSTOMER_TABLE + "." + DatabaseHelper.CUSTOMER_EMAIL + ", " +
                DatabaseHelper.CUSTOMER_TABLE + "." + DatabaseHelper.CUSTOMER_IMAGE + ", " +
                DatabaseHelper.RESERVATION_TABLE + "." + DatabaseHelper.RESERVATION_DATE + ", " +
                DatabaseHelper.CAR_TABLE + "." + DatabaseHelper.CAR_MODEL +
                " FROM " + DatabaseHelper.RESERVATION_TABLE +
                " INNER JOIN " + DatabaseHelper.CUSTOMER_TABLE +
                " ON " + DatabaseHelper.RESERVATION_TABLE + "." + DatabaseHelper.RESERVATION_EMAIL +
                " = " + DatabaseHelper.CUSTOMER_TABLE + "." + DatabaseHelper.CUSTOMER_EMAIL +
                " INNER JOIN " + DatabaseHelper.CAR_TABLE +
                " ON " + DatabaseHelper.RESERVATION_TABLE + "." + DatabaseHelper.RESERVATION_CAR_ID +
                " = " + DatabaseHelper.CAR_TABLE + "." + DatabaseHelper.CAR_ID;

        return database.rawQuery(query, null);
    }


    public Cursor getFavoriteCarsForUser(String userEmail) {
        userEmail = userEmail.toLowerCase();
        String query = "SELECT * FROM " + DatabaseHelper.CAR_TABLE +
                " INNER JOIN " + DatabaseHelper.FAVORITE_TABLE +
                " ON " + DatabaseHelper.CAR_TABLE + "." + DatabaseHelper.CAR_ID +
                " = " + DatabaseHelper.FAVORITE_TABLE + "." + DatabaseHelper.FAVORITE_CAR_ID +
                " WHERE " + DatabaseHelper.FAVORITE_TABLE + "." + DatabaseHelper.FAVORITE_EMAIL +
                " = '" + userEmail + "'";
        return database.rawQuery(query, null);
    }


    public boolean isEmailUsed(String email) {
        email = email.toLowerCase();
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
        email = email.toLowerCase();
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


    public Cursor customerInfo(String email) {
        email = email.toLowerCase();
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


    public Cursor getCarsByFilterAndNotInReservations(String[] makes, String[] fuels, int minYear, int maxYear, int minPrice, int maxPrice) {

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
                .append(") AND (CASE WHEN ")
                .append(DatabaseHelper.CAR_OFFER).append(" > 0 THEN (")
                .append(DatabaseHelper.CAR_OFFER).append(" BETWEEN ").append(minPrice).append(" AND ").append(maxPrice)
                .append(") ELSE (")
                .append(DatabaseHelper.CAR_PRICE).append(" BETWEEN ").append(minPrice).append(" AND ").append(maxPrice)
                .append(") END)");


        whereClause.append(" AND NOT EXISTS (")
                .append(" SELECT 1 FROM ").append(DatabaseHelper.RESERVATION_TABLE)
                .append(" WHERE ").append(DatabaseHelper.RESERVATION_TABLE).append(".").append(DatabaseHelper.RESERVATION_CAR_ID)
                .append(" = ").append(DatabaseHelper.CAR_TABLE).append(".").append(DatabaseHelper.CAR_ID)
                .append(")");

        Cursor cursor = database.query(DatabaseHelper.CAR_TABLE, null, whereClause.toString(), null, null, null, null);

        return cursor;
    }


    public void deleteCustomer(String customerEmail) {
        String whereClauseCustomer = DatabaseHelper.CUSTOMER_EMAIL + " = ?";
        String[] whereArgsCustomer = {customerEmail};
        database.delete(DatabaseHelper.CUSTOMER_TABLE, whereClauseCustomer, whereArgsCustomer);
        String whereClauseReservation = DatabaseHelper.RESERVATION_EMAIL + " = ?";
        database.delete(DatabaseHelper.RESERVATION_TABLE, whereClauseReservation, whereArgsCustomer);
    }

    public Cursor getCarsNotInReservationWithOffer() {
        String carTable = DatabaseHelper.CAR_TABLE;
        String reservationTable = DatabaseHelper.RESERVATION_TABLE;
        String subquery = "SELECT DISTINCT " + DatabaseHelper.RESERVATION_CAR_ID +
                " FROM " + reservationTable;
        String query = "SELECT * FROM " + carTable +
                " WHERE " + DatabaseHelper.CAR_ID + " NOT IN (" + subquery + ")" +
                " AND " + DatabaseHelper.CAR_OFFER + " > 0";
        return database.rawQuery(query, null);
    }


    public int getCarOffer(int carId) {
        int carOffer = 0;
        String selection = DatabaseHelper.CAR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(carId)};
        Cursor cursor = database.rawQuery("SELECT " + DatabaseHelper.CAR_OFFER + " FROM " + DatabaseHelper.CAR_TABLE +
                " WHERE " + selection, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            carOffer = cursor.getInt(0);

            cursor.close();
        }

        return carOffer;
    }
    public byte[] getCarImage(int carId) {
        byte[] carImage = null;
        String selection = DatabaseHelper.CAR_ID + " = ?";
        String[] selectionArgs = {String.valueOf(carId)};

        Cursor cursor = database.rawQuery("SELECT " + DatabaseHelper.CAR_IMAGE +
                " FROM " + DatabaseHelper.CAR_TABLE +
                " WHERE " + selection, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DatabaseHelper.CAR_IMAGE);
            carImage = cursor.getBlob(columnIndex);

            cursor.close();
        }

        return carImage;
    }




}