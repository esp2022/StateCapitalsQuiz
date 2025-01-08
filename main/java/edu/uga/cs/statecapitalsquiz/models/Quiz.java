/**
 * A class to represent a Quiz.
 *
 * <p>
 * A quiz consists of a unique identifier, a date, a score, a list of questions,
 * the user's answers, and the correct answers.
 *
 */
package edu.uga.cs.statecapitalsquiz.models;

import java.util.List;
import java.util.Date;

public class Quiz {

    /**
     * The unique identifier of the quiz.
     */
    private int quizId;

    /**
     * The date the quiz was taken.
     */
    private Date date;

    /**
     * The score of the quiz.
     */
    private int score;

    /**
     * The list of questions in the quiz.
     */
    private List<Question> questions;

    /**
     * The list of user's answers.
     */
    private List<String> userAnswers;

    /**
     * The list of correct answers.
     */
    private List<String> correctAnswers;

    /**
     * The index of the current question being displayed.
     */
    private int currentQuestionIndex;

    /**
     * The total number of questions answered by the user.
     */
    private int answeredQuestions;

    /**
     * Constructor to create a Quiz object.
     *
     * @param quizId   the unique identifier of the quiz
     * @param date     the date the quiz was taken
     * @param score    the score of the quiz
     * @param questions the list of questions in the quiz
     * @param userAnswers the list of user's answers
     * @param correctAnswers the list of correct answers
     */
    public Quiz(int quizId, Date date, int score, List<Question> questions, List<String> userAnswers,
            List<String> correctAnswers) {
        this.quizId = quizId;
        this.date = date;
        this.score = score;
        this.questions = questions;
        this.userAnswers = userAnswers;
        this.correctAnswers = correctAnswers;
    }

    /**
     * Getter for the quizId.
     *
     * @return the quizId
     */
    public int getQuizId() {
        return quizId;
    }

    /**
     * Setter for the quizId.
     *
     * @param quizId the quizId to set
     */
    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    /**
     * Getter for the date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Setter for the date.
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Getter for the score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Setter for the score.
     *
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Getter for the questions.
     *
     * @return the questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Setter for the questions.
     *
     * @param questions the questions to set
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    /**
     * Getter for the userAnswers.
     *
     * @return the userAnswers
     */
    public List<String> getUserAnswers() {
        return userAnswers;
    }

    /**
     * Setter for the userAnswers.
     *
     * @param userAnswers the userAnswers to set
     */
    public void setUserAnswers(List<String> userAnswers) {
        this.userAnswers = userAnswers;
    }

    /**
     * Getter for the correctAnswers.
     *
     * @return the correctAnswers
     */
    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    /**
     * Setter for the correctAnswers.
     *
     * @param correctAnswers the correctAnswers to set
     */
    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

/**
 * Gets the index of the current question being presented in the quiz.
 *
 * @return the index of the current question
 */
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    /**
     * Sets the index of the current question being presented in the quiz.
     *
     * @param currentQuestionIndex the index of the current question
     */
    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    /**
     * Returns the number of questions that the user has answered in the quiz.
     *
     * @return the number of questions that the user has answered
     */
    public int getAnsweredQuestions() {
        return answeredQuestions;
    }

    /**
     * Sets the number of questions that the user has answered in the quiz.
     *
     * @param answeredQuestions the number of questions that the user has answered
     */
    public void setAnsweredQuestions(int answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

}

