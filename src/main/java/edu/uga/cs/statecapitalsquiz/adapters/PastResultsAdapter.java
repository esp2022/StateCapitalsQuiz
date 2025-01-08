package edu.uga.cs.statecapitalsquiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import edu.uga.cs.statecapitalsquiz.R;
import edu.uga.cs.statecapitalsquiz.models.Quiz;

public class PastResultsAdapter extends RecyclerView.Adapter<PastResultsAdapter.ViewHolder> {

    /**
     * List of past quiz results to be displayed.
     */
    private final List<Quiz> pastResults;

    /**
     * Listener for when a past quiz result is clicked.
     */
    private final OnQuizClickListener listener;

    /**
     * Interface for when a past quiz result is clicked.
     */
    public interface OnQuizClickListener {
        /**
         * Called when a past quiz result is clicked.
         *
         * @param quiz The quiz that was clicked.
         */
        void onQuizClick(Quiz quiz);
    }

    /**
     * Constructor for PastResultsAdapter.
     *
     * @param pastResults List of past quiz results to be displayed.
     * @param listener    Listener for when a past quiz result is clicked.
     */
    public PastResultsAdapter(List<Quiz> pastResults, OnQuizClickListener listener) {
        this.pastResults = pastResults;
        this.listener = listener;
    }

        /**
         * Called when RecyclerView needs a new ViewHolder of the given type to represent
         * an item.
         * <p>
         * This new ViewHolder will be used to display items of the adapter using
         * {@link #onBindViewHolder(ViewHolder, int)}. Since it will be re-used to display
         * different items in the data set, it is a good idea to cache references to sub views of
         * the View to avoid unnecessary {@link View#findViewById(int)} calls.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         * @see #onBindViewHolder(ViewHolder, int)
         */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_result, parent, false);
        return new ViewHolder(view);
    }

/**
 * Binds the data of a quiz at the specified position to the provided ViewHolder.
 *
 * @param holder   The ViewHolder to bind data to.
 * @param position The position of the quiz in the adapter.
 */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = pastResults.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
        holder.resultDate.setText("Date: " + dateFormat.format(quiz.getDate()));
        holder.resultScore.setText("Score: " + quiz.getScore() + "/6");

        holder.itemView.setOnClickListener(v -> listener.onQuizClick(quiz));
    }

        /**
         * Returns the number of past quiz results in the adapter.
         *
         * @return the number of past quiz results in the adapter
         */
    @Override
    public int getItemCount() {
        return pastResults.size();
    }

    /**
     * ViewHolder class for PastResultsAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * TextView for displaying the date of the quiz.
         */
        TextView resultDate;

        /**
         * TextView for displaying the score of the quiz.
         */
        TextView resultScore;

        /**
         * Constructor for ViewHolder.
         *
         * @param itemView The view that the ViewHolder represents.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            resultDate = itemView.findViewById(R.id.resultDate);
            resultScore = itemView.findViewById(R.id.resultScore);
        }
    }
}
