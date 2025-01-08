package edu.uga.cs.statecapitalsquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.uga.cs.statecapitalsquiz.R;
import java.util.Collections;
import java.util.List;
import edu.uga.cs.statecapitalsquiz.models.Quiz;
import edu.uga.cs.statecapitalsquiz.models.Question;

/**
 * A fragment that displays the details of a quiz, including the score and the correct answers of each question.
 */
public class QuizDetailsFragment extends Fragment {

    private Quiz quiz;
    private List<Question> questions;
    private TextView quizDetailsTextView;
    private Button homeButton, pastResultsButton; 

    /**
     * Constructs a QuizDetailsFragment with the given quiz and questions.
     *
     * @param quiz     the quiz to be displayed
     * @param questions the questions of the quiz
     */
    public QuizDetailsFragment(Quiz quiz, List<Question> questions) {
        this.quiz = quiz;
        this.questions = questions != null ? questions : Collections.emptyList(); 
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_details, container, false);

        quizDetailsTextView = view.findViewById(R.id.quizDetailsTextView);
        homeButton = view.findViewById(R.id.homeButton);
        pastResultsButton = view.findViewById(R.id.pastResultsButton); 

        displayQuizDetails();

        homeButton.setOnClickListener(v -> navigateHome());

        // Set up navigation to PastResultsFragment
        pastResultsButton.setOnClickListener(v -> navigateToPastResults());

        return view;
    }

    /**
     * Displays the details of the quiz in the {@link #quizDetailsTextView}.
     */
    private void displayQuizDetails() {
        if (questions == null || questions.isEmpty()) {
            quizDetailsTextView.setText("No questions available.");
            return;
        }

        StringBuilder details = new StringBuilder();
        details.append("Total Score: ").append(quiz.getScore()).append("/6\n\n");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String userAnswer = quiz.getUserAnswers() != null && quiz.getUserAnswers().size() > i
                    ? quiz.getUserAnswers().get(i)
                    : "No answer provided";
            String correctAnswer = quiz.getCorrectAnswers() != null && quiz.getCorrectAnswers().size() > i
                    ? quiz.getCorrectAnswers().get(i)
                    : "No answer provided";

            details.append("Question ").append(i + 1).append(": ").append(question.getQuestionText()).append("\n");
            details.append("User's Answer: ").append(userAnswer).append("\n");
            details.append("Correct Answer: ").append(correctAnswer).append("\n\n");
        }

        quizDetailsTextView.setText(details.toString());
    }

    /**
     * Navigates to the {@link SplashFragment} when the home button is clicked.
     */
    private void navigateHome() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SplashFragment())
                    .commit();
        }
    }

    /**
     * Navigates to the {@link PastResultsFragment} when the past results button is clicked.
     */
    private void navigateToPastResults() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new PastResultsFragment())
                    .addToBackStack(null)  
                    .commit();
        }
    }
}

