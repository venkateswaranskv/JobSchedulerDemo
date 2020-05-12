package skven.com.jobscheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "skven:MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click(View view) {
        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
        scheduleJob(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                while (true) {
                    if(pm != null) {
                        Log.d(TAG, "run:isDeviceIdleMode " + pm.isDeviceIdleMode());
                        Log.d(TAG, "run:isInteractive " + pm.isInteractive());
                    }else {
                        Log.d(TAG, "run: pm is null");
                    }
                    try {
                        Thread.sleep(5_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

            }
        }).start();

    }

    public void scheduleJob(View v) {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);

        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresDeviceIdle(true)
                .setPeriodic(500)
                .setPersisted(true)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        if(scheduler.getPendingJob(123) == null) {
            int resultCode = scheduler.schedule(info);
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d(TAG, "Job scheduled");
                Toast.makeText(getApplicationContext(), "Job scheduled", Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Job scheduling failed");
                Toast.makeText(getApplicationContext(), "Job scheduling failed", Toast.LENGTH_LONG).show();
            }

        }else {
            scheduler.cancel(123);
            Toast.makeText(getApplicationContext(), "cancelled job", Toast.LENGTH_LONG).show();
        }
    }

    public void checkPendingJobStatus(View v) {

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        JobInfo pendingJob = scheduler.getPendingJob(123);
        final String status;
        if(pendingJob == null) {
            Log.d(TAG, "checkPendingJobStatus: job completed");
            status = "job executed";
        }else {
            Log.d(TAG, "checkPendingJobStatus: job has to run");
            status = "job not executed";
        }
        Toast.makeText(getApplicationContext(), status,Toast.LENGTH_LONG).show();
    }

}
