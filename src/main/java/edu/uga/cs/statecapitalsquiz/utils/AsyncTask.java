package edu.uga.cs.statecapitalsquiz.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This abstract class provides a framework for executing tasks asynchronously.
 * It uses Java's standard concurrency framework to avoid blocking the main UI thread.
 * Subclasses must implement the abstract methods to define the task's behavior.
 *
 * @param <Param>  the type of the input parameters
 * @param <Result> the type of the result produced by doInBackground
 */
public abstract class AsyncTask<Param, Result> {

    /**
     * Executes the task in a background thread.
     *
     * @param params the parameters of the task
     */
    private void executeInBackground(Param... params) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Result result = doInBackground(params);
            Looper looper = Looper.getMainLooper();
            Handler handler = new Handler(looper);
            handler.post(() -> onPostExecute(result));
        });
    }

    /**
     * Starts the execution of the task.
     *
     * @param arguments the input parameters of the task
     */
    public void execute(Param... arguments) {
        executeInBackground(arguments);
    }

    /**
     * Performs the task in the background.
     * Subclasses must override this method to define the task's behavior.
     *
     * @param arguments the input parameters of the task
     * @return the result of the task
     */
    protected abstract Result doInBackground(Param... arguments);

    /**
     * Called on the main UI thread after the background computation finishes.
     * Subclasses must override this method to define the post-execution behavior.
     *
     * @param result the result of the background computation
     */
    protected abstract void onPostExecute(Result result);
}

