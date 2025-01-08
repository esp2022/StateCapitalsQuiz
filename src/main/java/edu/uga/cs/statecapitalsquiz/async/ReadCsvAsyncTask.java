package edu.uga.cs.statecapitalsquiz.async;

import android.content.Context;
import android.util.Log;

import edu.uga.cs.statecapitalsquiz.utils.AsyncTask;
import edu.uga.cs.statecapitalsquiz.utils.CsvReader;

/**
 * ReadCsvAsyncTask reads a CSV file and inserts its data into the database
 * asynchronously.
 */
public class ReadCsvAsyncTask extends AsyncTask<String, Void> {
    private static final String TAG = "ReadCsvAsyncTask";
    private final Context context;

    public ReadCsvAsyncTask(Context context) {
        this.context = context;
    }

    /**
     * Reads a CSV file and inserts its data into the database asynchronously.
     *
     * @param params
     *            a single string parameter containing the name of the CSV file
     *            to read
     * @return null
     */
    @Override
    protected Void doInBackground(String... params) {
        String csvFileName = params[0];
        CsvReader.readAndInsertCsvData(context, csvFileName);
        return null;
    }

    /**
     * Notifies that the CSV reading and insertion has been completed.
     *
     * @param result
     *            unused parameter
     */
    @Override
    protected void onPostExecute(Void result) {
        Log.d(TAG, "CSV reading and insertion completed.");
    }
}
