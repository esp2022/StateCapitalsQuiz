/**
 * The main activity of the State Capitals Quiz app.
 * 
 * This activity displays the splash screen fragment and checks if the database
 * exists. If the database does not exist, it runs an asynchronous task to read
 * the CSV file and insert its data into the database.
 * 
 */
package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import java.io.File;

import edu.uga.cs.statecapitalsquiz.fragments.SplashFragment;
import edu.uga.cs.statecapitalsquiz.async.ReadCsvAsyncTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if the fragment container is empty and add the initial fragment
        // (SplashFragment)
        if (savedInstanceState == null) {
            Fragment splashFragment = new SplashFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, splashFragment)
                    .commit();
        }

        // Check if the database exists; if not, run the async task to read and insert
        // CSV data
        File databasePath = getDatabasePath("stateCapitalsQuiz.db");
        if (!databasePath.exists()) {
            Log.d(TAG, "Database does not exist. Starting CSV reading task.");
            new ReadCsvAsyncTask(this).execute("state_capitals.csv");
        } else {
            Log.d(TAG, "Database already exists. Skipping CSV reading.");
        }
    }
}

