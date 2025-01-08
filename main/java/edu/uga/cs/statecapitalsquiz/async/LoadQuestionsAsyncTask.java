package edu.uga.cs.statecapitalsquiz.async;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.uga.cs.statecapitalsquiz.database.QuizDBHelper;
import edu.uga.cs.statecapitalsquiz.models.Question;
import edu.uga.cs.statecapitalsquiz.utils.AsyncTask;

/**
 * Loads all questions from the database asynchronously.
 */
public class LoadQuestionsAsyncTask extends AsyncTask<Void, List<Question>> {
    private final Context context;
    private final OnQuestionsLoadedListener listener;

    /**
     * Constructs a new LoadQuestionsAsyncTask with the given context and listener.
     *
     * @param context  the context to use for database operations
     * @param listener the listener to notify when the questions are loaded
     */
    public LoadQuestionsAsyncTask(Context context, OnQuestionsLoadedListener listener) {
        this.context = context;
        this.listener = listener;
    }

        /**
         * Loads all questions from the database asynchronously.
         * 
         * @return a list of all questions from the database
         */
    @Override
    protected List<Question> doInBackground(Void... voids) {
        List<Question> questions = new ArrayList<>();
        QuizDBHelper dbHelper = new QuizDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM states";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int stateNameIndex = cursor.getColumnIndex("state_name");
            int capitalIndex = cursor.getColumnIndex("capital");
            int city1Index = cursor.getColumnIndex("city1");
            int city2Index = cursor.getColumnIndex("city2");

            if (idIndex != -1 && stateNameIndex != -1 && capitalIndex != -1 && city1Index != -1 && city2Index != -1) {
                do {
                    String stateName = cursor.getString(stateNameIndex);
                    String capital = cursor.getString(capitalIndex);
                    String city1 = cursor.getString(city1Index);
                    String city2 = cursor.getString(city2Index);

                    List<String> options = new ArrayList<>();
                    options.add(capital);
                    options.add(city1);
                    options.add(city2);
                    Collections.shuffle(options);

                    Question question = new Question(
                            "What is the capital of " + stateName + "?",
                            options.get(0),
                            options.get(1),
                            options.get(2),
                            options.indexOf(capital));
                    questions.add(question);
                } while (cursor.moveToNext());
            } else {
                Log.e("LoadQuestionsAsyncTask", "One or more columns not found in the database.");
            }
            cursor.close();
        } else {
            Log.e("LoadQuestionsAsyncTask", "Cursor is null or empty.");
        }

        db.close();
        return questions;
    }

/**
 * Notifies the listener that the questions have been loaded.
 *
 * @param result the list of Question objects containing the loaded questions
 */
    @Override
    protected void onPostExecute(List<Question> result) {
        listener.onQuestionsLoaded(result);
    }

    /**
     * Interface for classes that want to be notified when the questions are loaded.
     */
    public interface OnQuestionsLoadedListener {
        /**
         * Called when the questions are loaded.
         *
         * @param questions the loaded questions
         */
        void onQuestionsLoaded(List<Question> questions);
    }
}

