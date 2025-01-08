package edu.uga.cs.statecapitalsquiz.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.uga.cs.statecapitalsquiz.R;
import edu.uga.cs.statecapitalsquiz.async.LoadQuizDetailsAsyncTask;
import edu.uga.cs.statecapitalsquiz.async.SaveQuizResultAsyncTask;
import edu.uga.cs.statecapitalsquiz.async.SaveUserAnswerAsyncTask;
import edu.uga.cs.statecapitalsquiz.models.Question;
import edu.uga.cs.statecapitalsquiz.models.Quiz;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * A fragment that displays a single question from a quiz. The user can
 * select one of the answer options, and the fragment will display the
 * next question in the quiz. If the user has completed all questions
 * in the quiz, the quiz results are shown.
 */
public class QuizFragment extends Fragment {

    private TextView questionTextView;
    private TextView questionProgressTextView;  
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3;
    private Button nextButton, quitButton;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private GestureDetector gestureDetector;
    private long quizId;  
    public QuizFragment(long quizId, List<Question> questions) {
        this.quizId = quizId;
        this.questions = questions;
    }
    /**
     * Called to have the fragment instantiate its user interface view.
     * <p>
     * This is optional and non-graphical Components can skip it. This will be
     * called between {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p>
     * It is recommended to only inflate the layout in this method and move
     * logic that operates on the returned View to {@link #onViewCreated(View,
     * Bundle)}.
     * <p>
     * If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     * <p>
     * @param inflater           The LayoutInflater object that can be used to
     *                            inflate any views in the fragment,
     * @param container          If non-null, this is the parent view that the
     *                            fragment's UI should be attached to. The
     *                            fragment should not add the view itself, but
     *                            this can be used to generate the LayoutParams
     *                            of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                            from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        questionTextView = view.findViewById(R.id.questionTextView);
        questionProgressTextView = view.findViewById(R.id.questionProgressTextView); // Initialize this
        optionsGroup = view.findViewById(R.id.optionsGroup);
        option1 = view.findViewById(R.id.option1);
        option2 = view.findViewById(R.id.option2);
        option3 = view.findViewById(R.id.option3);
        nextButton = view.findViewById(R.id.nextQuestionButton);
        quitButton = view.findViewById(R.id.quitQuizButton);

        gestureDetector = new GestureDetector(getContext(), new GestureListener());

        displayQuestion(currentQuestionIndex);

        nextButton.setOnClickListener(v -> proceedToNextQuestion());

        quitButton.setOnClickListener(v -> showQuitConfirmationDialog());

        view.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        return view;
    }

    /**
     * Displays the question at the given index, updating the question progress text and
     * setting the text of the question and options.
     *
     * @param index the index of the question to display
     */
    private void displayQuestion(int index) {
        // Update the question progress text
        String progressText = "Question " + (index + 1) + " of " + questions.size();
        questionProgressTextView.setText(progressText);

        // Display the current question and options
        Question question = questions.get(index);
        questionTextView.setText(question.getQuestionText());
        option1.setText("1. " + question.getOption1());
        option2.setText("2. " + question.getOption2());
        option3.setText("3. " + question.getOption3());
        optionsGroup.clearCheck();
    }

    /**
     * Proceeds to the next question in the quiz, after checking the answer to the current question.
     * If the user has not selected an answer, a toast is displayed asking them to do so.
     * If the user has selected an answer, the correctness of the answer is checked and saved,
     * and then the next question is displayed. If the user has completed all questions,
     * the quiz results are shown.
     */
    private void proceedToNextQuestion() {
        if (optionsGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getContext(), "Please select an answer", Toast.LENGTH_SHORT).show();
        } else {
            checkAnswer();
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayQuestion(currentQuestionIndex);
            } else {
                showQuizResults();
            }
        }
    }

    /**
     * Checks the user's selected answer for the current question.
     * Identifies which option the user selected and determines whether
     * it is correct by comparing it to the correct answer index of the 
     * current question. The user's answer is then saved asynchronously 
     * and, if correct, increments the local score.
     */
    private void checkAnswer() {
        Question question = questions.get(currentQuestionIndex);
        int selectedOptionIndex = -1;

        if (option1.isChecked()) {
            selectedOptionIndex = 0;
        } else if (option2.isChecked()) {
            selectedOptionIndex = 1;
        } else if (option3.isChecked()) {
            selectedOptionIndex = 2;
        }

        if (selectedOptionIndex != -1) {
            String userAnswer = question.getOptions().get(selectedOptionIndex);
            boolean isCorrect = selectedOptionIndex == question.getCorrectAnswerIndex();

            // Save the user answer asynchronously
            new SaveUserAnswerAsyncTask(getContext()).execute(quizId, currentQuestionIndex, userAnswer, isCorrect);

            // Increment the score locally if the answer is correct
            if (isCorrect) {
                score++;
            }
        }
    }

    /**
     * Navigates to the QuizDetailsFragment and displays the quiz results.
     * 
     * Updates the quiz results asynchronously by saving the score and current date
     * to the database, and then loads the quiz details asynchronously, which
     * includes the questions and their correct answers, user answers, and the score.
     * 
     * This method is called when the user finishes the quiz.
     */
    private void showQuizResults() {
        // Calculate total answered questions
        int totalAnsweredQuestions = currentQuestionIndex + 1; // Assuming 0-based index

        // Prepare the current date string
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Execute the AsyncTask to update the quiz results asynchronously
        new SaveQuizResultAsyncTask(getContext()).execute(quizId, score, currentQuestionIndex, totalAnsweredQuestions, currentDate);

        // Navigate to QuizDetailsFragment after loading quiz data
        new LoadQuizDetailsAsyncTask(getContext(), quiz -> {
            if (quiz != null && getActivity() != null) {
                QuizDetailsFragment quizDetailsFragment = new QuizDetailsFragment(quiz, quiz.getQuestions());
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, quizDetailsFragment)
                        .commit();
            }
        }).execute((int) quizId);
    }

    /**
     * Shows a confirmation dialog asking the user if they want to quit the quiz.
     * 
     * If the user clicks "Yes", the quiz is ended and the user is navigated
     * back to the main fragment or splash fragment. If the user clicks "No",
     * the dialog is dismissed and the quiz continues.
     */
    private void showQuitConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Quit Quiz")
                .setMessage("Are you sure you want to quit the quiz?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Navigate back to the main fragment or splash fragment
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new SplashFragment())
                                .commit();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Called when the fragment is no longer resumed. This is typically
     * called when the current fragment is being removed or replaced.
     * <p>
     * Save the current quiz progress to the database or SharedPreferences
     * so that the user can resume the quiz later.
     */
    @Override
    public void onPause() {
        super.onPause();
        int totalAnsweredQuestions = currentQuestionIndex + 1; // Calculate based on progress
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Save the quiz progress to the database or SharedPreferences
        new SaveQuizResultAsyncTask(getContext()).execute(quizId, score, currentQuestionIndex, totalAnsweredQuestions, currentDate);
    }


    /**
     * Called when the fragment is resumed. This method reloads the quiz state from the database
     * to continue the quiz from where the user left off. If a partial quiz is found, it restores
     * the current question index and score, and displays the current question. If no partial 
     * quiz is found, a new quiz is started.
     */
    @Override
    public void onResume() {
        super.onResume();
        // Load the quiz state from the database
        new LoadQuizDetailsAsyncTask(getContext(), quiz -> {
            if (quiz != null && quiz.getAnsweredQuestions() < quiz.getQuestions().size()) {
                currentQuestionIndex = quiz.getCurrentQuestionIndex();
                score = quiz.getScore();
                displayQuestion(currentQuestionIndex);
            } else {
                // Start a new quiz if no partial quiz found
            }
        }).execute((int) quizId);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX < 0) {
                        proceedToNextQuestion();
                    }
                    return true;
                }
            }
            return false;
        }

        /**
         * {@inheritDoc}
         *
         * <p>Returning true to allow the detector to handle gestures.
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return true; // Return true to handle gestures
        }

        /**
         * Called when the user taps once on the screen.
         *
         * @param e The motion event.
         * @return True if the event was handled, false otherwise.
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        /**
         * {@inheritDoc}
         *
         * <p>This method is not used in this fragment.
         */
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }
    }
}

