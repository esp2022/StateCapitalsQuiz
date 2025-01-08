package edu.uga.cs.statecapitalsquiz.fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import edu.uga.cs.statecapitalsquiz.R;
import edu.uga.cs.statecapitalsquiz.async.InsertQuizAsyncTask;
import edu.uga.cs.statecapitalsquiz.async.LoadQuestionsAsyncTask;
import edu.uga.cs.statecapitalsquiz.models.Question;
import edu.uga.cs.statecapitalsquiz.models.Quiz;

/**
 * A fragment that displays the splash screen of the app, including a button to start a new quiz and
 * a button to view past results.
 */
public class SplashFragment extends Fragment {

    private Button startQuizButton;
    private Button viewResultsButton;

    /**
     * Called when the fragment is created. Inflates the layout and sets up the button listeners.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        startQuizButton = view.findViewById(R.id.startQuizButton);
        viewResultsButton = view.findViewById(R.id.viewResultsButton);

        startQuizButton.setOnClickListener(v -> startNewQuiz());
        viewResultsButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new PastResultsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    /**
     * Starts a new quiz by loading the questions from the database and saving a new quiz to the database.
     */
    private void startNewQuiz() {
        new LoadQuestionsAsyncTask(getContext(), questions -> {
            if (questions.isEmpty()) {
                Toast.makeText(getContext(), "Failed to start a new quiz. Please check the database.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Shuffle and select 6 unique questions
            Collections.shuffle(questions, new Random());
            List<Question> selectedQuestions = questions.subList(0, Math.min(6, questions.size()));

            Quiz newQuiz = new Quiz(-1, new Date(), 0, selectedQuestions, null, null);
            saveNewQuizToDatabase(newQuiz, selectedQuestions);
        }).execute();
    }

    /**
     * Saves a new quiz to the database.
     *
     * @param quiz The quiz to save.
     * @param questions The questions that the quiz contains.
     */
    private void saveNewQuizToDatabase(Quiz quiz, List<Question> questions) {
        ContentValues values = new ContentValues();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        values.put("date", currentDate);
        values.put("score", quiz.getScore());

        // Save each question and its correct answer
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            values.put("question" + (i + 1), question.getQuestionText());
            values.put("correct_answer" + (i + 1), question.getOptions().get(question.getCorrectAnswerIndex()));
        }

        new InsertQuizAsyncTask(getContext(), quizId -> {
            if (quizId == -1) {
                Log.e("SplashFragment", "Failed to insert new quiz into database.");
            } else {
                Log.d("SplashFragment", "New quiz created with ID: " + quizId);
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new QuizFragment(quizId, questions))
                            .addToBackStack(null)
                            .commit();
                }
            }
        }).execute(values);
    }

}