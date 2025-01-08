package edu.uga.cs.statecapitalsquiz.models;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a question in the State Capitals Quiz app.
 */
public class Question {

    /**
     * The text of the question.
     */
    private final String questionText;

    /**
     * The first option for the question.
     */
    private final String option1;

    /**
     * The second option for the question.
     */
    private final String option2;

    /**
     * The third option for the question.
     */
    private final String option3;

    /**
     * The index of the correct answer for the question.
     */
    private final int correctAnswerIndex;

    /**
     * Constructor for a Question object.
     * 
     * @param questionText the text of the question
     * @param option1 the first option
     * @param option2 the second option
     * @param option3 the third option
     * @param correctAnswerIndex the index of the correct answer
     */
    public Question(String questionText, String option1, String option2, String option3, int correctAnswerIndex) {
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    /**
     * Returns the text of the question.
     *
     * @return the text of the question
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Returns the first option for the question.
     *
     * @return the first option
     */
    public String getOption1() {
        return option1;
    }

    /**
     * Returns the second option for the question.
     *
     * @return the second option
     */
    public String getOption2() {
        return option2;
    }

    /**
     * Returns the third option for the question.
     *
     * @return the third option
     */
    public String getOption3() {
        return option3;
    }

    /**
     * Returns the index of the correct answer for the question.
     *
     * @return the index of the correct answer
     */
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    /**
     * Returns a list of the options for the question.
     *
     * @return a list of the options
     */
    public List<String> getOptions() {
        return Arrays.asList(option1, option2, option3);
    }
}

