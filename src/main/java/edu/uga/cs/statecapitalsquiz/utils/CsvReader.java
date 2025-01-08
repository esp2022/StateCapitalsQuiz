package edu.uga.cs.statecapitalsquiz.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.uga.cs.statecapitalsquiz.database.QuizDBHelper;

/**
 * Utility class to read a CSV file and insert its data into the database.
 */
public class CsvReader {

    private static final String TAG = "CsvReader";

    /**
     * Reads a CSV file and inserts its data into the database.
     *
     * @param context the application context
     * @param csvFileName the name of the CSV file to read
     */
    public static void readAndInsertCsvData(Context context, String csvFileName) {
        QuizDBHelper dbHelper = new QuizDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        AssetManager assetManager = context.getAssets();
        InputStream inputStream;

        try {
            inputStream = assetManager.open(csvFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            boolean isFirstLine = true; // To skip the header line
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }

                // Split the line into values using comma as the delimiter
                String[] values = line.split(",");

                // Ensuring there are enough columns in the line
                if (values.length >= 4) {
                    String stateName = values[0].trim();
                    String capital = values[1].trim();
                    String city1 = values[2].trim();
                    String city2 = values[3].trim();

                    // Insert the data into the database
                    String insertQuery = "INSERT INTO states (state_name, capital, city1, city2) VALUES (?, ?, ?, ?)";
                    db.execSQL(insertQuery, new String[]{stateName, capital, city1, city2});
                } else {
                    Log.e(TAG, "Invalid line format: " + line);
                }
            }

            reader.close();
            db.close();
            Log.d(TAG, "CSV data inserted successfully");

        } catch (IOException e) {
            Log.e(TAG, "Error reading CSV file", e);
        }
    }
}

