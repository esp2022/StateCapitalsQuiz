package edu.uga.cs.statecapitalsquiz.async;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.uga.cs.statecapitalsquiz.database.QuizDBHelper;
import edu.uga.cs.statecapitalsquiz.utils.AsyncTask;

/**
 * AsyncTask to save user answers asynchronously in the database.
 */
public class SaveUserAnswerAsyncTask extends AsyncTask<Object, Void> {
    private final Context context;

    public SaveUserAnswerAsyncTask(Context context) {
        this.context = context;
    }

    /**
     * Saves the user's answer to the quiz asynchronously in the database.
     * 
     * @param params an array of parameters, where:
     *               params[0] is the ID of the quiz
     *               params[1] is the index of the question
     *               params[2] is the user's answer for the question
     *               params[3] is a boolean indicating whether the answer is correct
     * @return null
     */
    @Override
    protected Void doInBackground(Object... params) {
        long quizId = (long) params[0];
        int questionIndex = (int) params[1];
        String userAnswer = (String) params[2];
        boolean isCorrect = (boolean) params[3];

        QuizDBHelper dbHelper = new QuizDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_answer" + (questionIndex + 1), userAnswer);

        // If the answer is correct, increment the score
        if (isCorrect) {
            db.execSQL("UPDATE quizzes SET score = score + 1 WHERE id = ?", new String[] { String.valueOf(quizId) });
        }

        // Update the user answer in the quiz table
        long result = db.update("quizzes", values, "id = ?", new String[] { String.valueOf(quizId) });
        if (result == -1) {
            System.err.println("Failed to update user answer for question " + (questionIndex + 1));
        } else {
            System.out.println("User answer for question " + (questionIndex + 1) + " updated successfully");
        }

        db.close();
        return null;
    }

        /**
         * No additional action needed after saving the answer.
         */
    @Override
    protected void onPostExecute(Void result) {
        // No additional action needed after saving the answer
    }
}
