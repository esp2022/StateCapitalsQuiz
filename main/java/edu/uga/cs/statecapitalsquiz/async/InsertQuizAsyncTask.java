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
 * AsyncTask to insert a new quiz into the database asynchronously.
 */
public class InsertQuizAsyncTask extends AsyncTask<ContentValues, Long> {
    private final Context context;
    private final OnQuizInsertedListener listener;

    /**
     * Constructor.
     *
     * @param context  the context
     * @param listener the listener to notify when the quiz is inserted
     */
    public InsertQuizAsyncTask(Context context, OnQuizInsertedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    /**
     * Inserts a new quiz into the database asynchronously.
     *
     * @param values a ContentValues object containing the quiz details
     * @return the ID of the inserted quiz
     */
    @Override
    protected Long doInBackground(ContentValues... values) {
        QuizDBHelper dbHelper = new QuizDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long quizId = db.insert("quizzes", null, values[0]);
        db.close();
        return quizId;
    }

    /**
     * Notifies the listener that the quiz has been inserted into the database.
     *
     * @param quizId the ID of the inserted quiz
     */
    @Override
    protected void onPostExecute(Long quizId) {
        listener.onQuizInserted(quizId);
    }

    /**
     * Listener to notify when the quiz is inserted.
     */
    public interface OnQuizInsertedListener {
        /**
         * Called when the quiz is inserted.
         *
         * @param quizId the ID of the inserted quiz
         */
        void onQuizInserted(long quizId);
    }
}
