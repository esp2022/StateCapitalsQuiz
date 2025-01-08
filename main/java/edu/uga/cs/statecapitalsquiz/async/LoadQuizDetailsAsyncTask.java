package edu.uga.cs.statecapitalsquiz.async;

import android.content.Context;
import android.os.AsyncTask;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import edu.uga.cs.statecapitalsquiz.database.QuizDBHelper;
import edu.uga.cs.statecapitalsquiz.models.Quiz;
import edu.uga.cs.statecapitalsquiz.models.Question;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/**
 * An asynchronous task to load a quiz from the database.
 * 
= *
 */
public class LoadQuizDetailsAsyncTask extends AsyncTask<Integer, Void, Quiz> {
    private Context context;
    private LoadQuizDetailsListener listener;

    public interface LoadQuizDetailsListener {
        void onQuizLoaded(Quiz quiz);
    }

    public LoadQuizDetailsAsyncTask(Context context, LoadQuizDetailsListener listener) {
        this.context = context;
        this.listener = listener;
    }

        /**
         * Loads a quiz from the database based on the provided quiz ID.
         *
         * @param quizIds an array of integers where the first element is the quiz ID
         * @return the loaded quiz
         */
        @Override
        protected Quiz doInBackground(Integer... quizIds) {
            int quizId = quizIds[0];
            Log.d("quizid", String.valueOf(quizId));
            QuizDBHelper dbHelper = new QuizDBHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM quizzes WHERE id = ?", new String[] { String.valueOf(quizId) });
            Quiz quiz = null;

            if (cursor != null && cursor.moveToFirst()) {
                Date date;
                try {
                    String dateString = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    date = dateFormat.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = new Date(); 
                }

                int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));

                // Fetch current question index and answered questions
                int currentQuestionIndex = cursor.getInt(cursor.getColumnIndexOrThrow("current_question_index"));
                int answeredQuestions = cursor.getInt(cursor.getColumnIndexOrThrow("answered_questions"));

                List<String> userAnswers = new ArrayList<>();
                List<String> correctAnswers = new ArrayList<>();
                List<Question> questions = new ArrayList<>();

                for (int i = 1; i <= 6; i++) {
                    String userAnswer = cursor.getString(cursor.getColumnIndexOrThrow("user_answer" + i));
                    String correctAnswer = cursor.getString(cursor.getColumnIndexOrThrow("correct_answer" + i));
                    String questionText = cursor.getString(cursor.getColumnIndexOrThrow("question" + i));

                    if (questionText != null) {
                        questions.add(new Question(questionText, null, null, null, -1));
                    }

                    userAnswers.add(userAnswer != null ? userAnswer : "N/A");
                    correctAnswers.add(correctAnswer != null ? correctAnswer : "N/A");
                }

                // Create the Quiz object and set the additional state data
                quiz = new Quiz(quizId, date, score, questions, userAnswers, correctAnswers);
                quiz.setCurrentQuestionIndex(currentQuestionIndex);
                quiz.setAnsweredQuestions(answeredQuestions);

                cursor.close();
            }

            db.close();
            return quiz;
        }

        /**
         * Notifies the listener that the quiz details have been loaded.
         * 
         * @param quiz the Quiz object containing the quiz details, or null if the quiz could not be found
         */
    @Override
    protected void onPostExecute(Quiz quiz) {
        if (listener != null) {
            listener.onQuizLoaded(quiz);
        }
    }
}
