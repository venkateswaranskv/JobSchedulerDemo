package skven.com.jobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by skven on 27/11/19.
 */

public class ExampleJobService extends JobService {
    private static final String TAG = "skven:ExampleJobService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");
        doBackgroundWork(params);

        return true;
    }

    private void doBackgroundWork(final JobParameters params) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences count = getApplicationContext().getSharedPreferences("count", MODE_PRIVATE);
                int count1 = count.getInt("count", 0);
                count.edit().putInt("count", (count1+1)).apply();
                Log.d(TAG, "Count value");

                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}