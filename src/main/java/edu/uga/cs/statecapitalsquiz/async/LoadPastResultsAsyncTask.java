package edu.uga.cs.statecapitalsquiz.async;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.uga.cs.statecapitalsquiz.database.QuizDBHelper;
import edu.uga.cs.statecapitalsquiz.models.Question;
import edu.uga.cs.statecapitalsquiz.models.Quiz;
import edu.uga.cs.statecapitalsquiz.utils.AsyncTask;

/**
 * An asynchronous task to load past quiz results from the database.
 *
 */
public class LoadPastResultsAsyncTask extends AsyncTask<Void, List<Quiz>> {
    private final Context context;
    private final OnResultsLoadedListener listener;

    /**
     * Constructor for LoadPastResultsAsyncTask.
     *
     * @param context the context to use for the database operations
     * @param listener the listener to notify when the results are loaded
     */
    public LoadPastResultsAsyncTask(Context context, OnResultsLoadedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * Loads the past quiz results from the database.
     *
     * @param voids unused parameter
     * @return a list of Quiz objects containing the quiz results
     */
    @Override
    protected List<Quiz> doInBackground(Void... voids) {
        List<Quiz> results = new ArrayList<>();
        QuizDBHelper dbHelper = new QuizDBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT id, date, score, question1, question2, question3, question4, question5, question6, " +
                "user_answer1, user_answer2, user_answer3, user_answer4, user_answer5, user_answer6, " +
                "correct_answer1, correct_answer2, correct_answer3, correct_answer4, correct_answer5, correct_answer6 " +
                "FROM quizzes ORDER BY date DESC";
        Cursor cursor = db.rawQuery(query, null);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int dateIndex = cursor.getColumnIndex("date");
            int scoreIndex = cursor.getColumnIndex("score");

            if (idIndex != -1 && dateIndex != -1 && scoreIndex != -1) {
                do {
                    int id = cursor.getInt(idIndex);
                    String dateString = cursor.getString(dateIndex);
                    int score = cursor.getInt(scoreIndex);

                    Date date = null;
                    try {
                        date = sdf.parse(dateString);
                    } catch (ParseException e) {
                        Log.e("LoadPastResultsAsyncTask", "Date parsing error: " + e.getMessage());
                    }

                    // Retrieve question texts, user answers, and correct answers
                    List<Question> questions = new ArrayList<>();
                    List<String> userAnswers = new ArrayList<>();
                    List<String> correctAnswers = new ArrayList<>();

                    for (int i = 1; i <= 6; i++) {
                        int questionIndex = cursor.getColumnIndex("question" + i);
                        int userAnswerIndex = cursor.getColumnIndex("user_answer" + i);
                        int correctAnswerIndex = cursor.getColumnIndex("correct_answer" + i);

                        String questionText = (questionIndex != -1) ? cursor.getString(questionIndex) : null;
                        String userAnswer = (userAnswerIndex != -1) ? cursor.getString(userAnswerIndex) : null;
                        String correctAnswer = (correctAnswerIndex != -1) ? cursor.getString(correctAnswerIndex) : null;


                        userAnswers.add(userAnswer != null ? userAnswer : "N/A");
                        correctAnswers.add(correctAnswer != null ? correctAnswer : "N/A"); // Store actual correct answer

                        // Create a Question object using the question text and placeholder options
                        if (questionText != null) {
                            questions.add(new Question(questionText, "Option1", "Option2", "Option3", 0));  
                        }
                    }

                    Quiz quiz = new Quiz(id, date, score, questions, userAnswers, correctAnswers);
                    results.add(quiz);
                } while (cursor.moveToNext());
            } else {
                Log.e("LoadPastResultsAsyncTask", "One or more columns not found in the database.");
            }
            cursor.close();
        } else {
            Log.e("LoadPastResultsAsyncTask", "Cursor is null or empty.");
        }

        db.close();
        return results;
    }

    /**
     * Notifies the listener that the results have been loaded.
     *
     * @param result the list of Quiz objects containing the quiz results
     */
    @Override
    protected void onPostExecute(List<Quiz> result) {
        listener.onResultsLoaded(result);
    }

    /**
     * Interface for listeners to be notified when the past quiz results are loaded.
     */
    public interface OnResultsLoadedListener {
        /**
         * Called when the past quiz results are loaded.
         *
         * @param results the list of Quiz objects containing the quiz results
         */
        void onResultsLoaded(List<Quiz> results);
    }
}
