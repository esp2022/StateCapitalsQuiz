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
 * AsyncTask to save quiz results asynchronously in the database.
 */
public class SaveQuizResultAsyncTask extends AsyncTask<Object, Void> {
    private final Context context;

    public SaveQuizResultAsyncTask(Context context) {
        this.context = context;
    }

    /**
     * Inserts a new quiz result into the database asynchronously.
     *
     * @param params an array of objects where the first element is the quiz ID,
     *               the second element is the score, the third element is the
     *               total number of questions and the fourth element is the
     *               current date
     * @return null
     */
    @Override
    protected Void doInBackground(Object... params) {
        long quizId = (long) params[0];
        int score = (int) params[1];
        int currentQuestionIndex = (int) params[2];
        int totalAnsweredQuestions = (int) params[3];
        String currentDate = (String) params[4]; // If needed

        QuizDBHelper dbHelper = new QuizDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("score", score);
        values.put("current_question_index", currentQuestionIndex);
        values.put("answered_questions", totalAnsweredQuestions);

        db.update("quizzes", values, "id = ?", new String[] { String.valueOf(quizId) });
        db.close();
        return null;
    }

    /**
     * No additional action needed after saving the result.
     */
    @Override
    protected void onPostExecute(Void result) {
        // No additional action needed after saving the result
    }
}
