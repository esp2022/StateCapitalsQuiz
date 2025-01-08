package edu.uga.cs.statecapitalsquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uga.cs.statecapitalsquiz.R;
import edu.uga.cs.statecapitalsquiz.adapters.PastResultsAdapter;
import edu.uga.cs.statecapitalsquiz.async.LoadPastResultsAsyncTask;
import edu.uga.cs.statecapitalsquiz.models.Quiz;

/**
 * A fragment that displays a list of past quiz results.
 */
public class PastResultsFragment extends Fragment {

    /**
     * The RecyclerView that displays the past quiz results.
     */
    private RecyclerView recyclerView;

    /**
     * The adapter that provides the data for the RecyclerView.
     */
    private PastResultsAdapter adapter;

    /**
     * The Home button that navigates the user back to the Home screen.
     */
    private Button homeButton;

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
     *
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_results, container, false);

        // Initialize the RecyclerView and set its layout manager
        recyclerView = view.findViewById(R.id.recyclerViewPastResults);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the Home button
        homeButton = view.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> navigateHome());

        // Load past quiz results from the database asynchronously
        loadPastResultsAsync();

        return view;
    }

    /**
     * Loads past quiz results from the database asynchronously using
     * {@link LoadPastResultsAsyncTask}.
     */
    private void loadPastResultsAsync() {
        new LoadPastResultsAsyncTask(getContext(), results -> {
            // Set up the adapter with the loaded results and assign it to the RecyclerView
            adapter = new PastResultsAdapter(results, quiz -> {
                if (getActivity() != null) {
                    // Replace with QuizDetailsFragment and pass the selected quiz
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, new QuizDetailsFragment(quiz, quiz.getQuestions()))
                            .addToBackStack(null)
                            .commit();
                }
            });
            recyclerView.setAdapter(adapter);
        }).execute();
    }

    /**
     * Navigates the user back to the Home screen.
     */
    private void navigateHome() {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SplashFragment())
                    .commit();
        }
    }
}

