package edu.uga.cs.statecapitalsquiz.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * A helper class to manage the database for the State Capitals Quiz app.
 * The database name is "stateCapitalsQuiz.db", and the version number is 1.
 * The class extends SQLiteOpenHelper and provides methods to create the
 * database schema and upgrade the database.
 *
 */
public class QuizDBHelper extends SQLiteOpenHelper {

    /**
     * The name of the database file.
     */
    private static final String DATABASE_NAME = "stateCapitalsQuiz.db";

    /**
     * The version number of the database.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Creates an instance of QuizDBHelper.
     *
     * @param context the context of the calling activity
     */
    public QuizDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is updated. Drops the existing tables and
     * calls {@link #onCreate(SQLiteDatabase)} to create the new tables.
     *
     * @param db the database to upgrade
     * @param oldVersion the old version number of the database
     * @param newVersion the new version number of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS states");
        db.execSQL("DROP TABLE IF EXISTS quizzes");
        onCreate(db);
    }

    /**
     * Called when the database is created. Creates the states and quizzes
     * tables.
     *
     * @param db the database to create
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STATES_TABLE = "CREATE TABLE states (" +
                "id INTEGER PRIMARY KEY, " +
                "state_name TEXT, " +
                "capital TEXT, " +
                "city1 TEXT, " +
                "city2 TEXT" +
                ")";

        String CREATE_QUIZZES_TABLE = "CREATE TABLE quizzes (" +
                "id INTEGER PRIMARY KEY, " +
                "date TEXT, " +
                "score INTEGER, " +
                "current_question_index INTEGER DEFAULT 0, " + 
                "answered_questions INTEGER DEFAULT 0, " + 
                "question1 TEXT, " +
                "question2 TEXT, " +
                "question3 TEXT, " +
                "question4 TEXT, " +
                "question5 TEXT, " +
                "question6 TEXT, " +
                "user_answer1 TEXT, " +
                "user_answer2 TEXT, " +
                "user_answer3 TEXT, " +
                "user_answer4 TEXT, " +
                "user_answer5 TEXT, " +
                "user_answer6 TEXT, " +
                "correct_answer1 TEXT, " +
                "correct_answer2 TEXT, " +
                "correct_answer3 TEXT, " +
                "correct_answer4 TEXT, " +
                "correct_answer5 TEXT, " +
                "correct_answer6 TEXT" +
                ")";

        db.execSQL(CREATE_STATES_TABLE);
        db.execSQL(CREATE_QUIZZES_TABLE);
    }
}
