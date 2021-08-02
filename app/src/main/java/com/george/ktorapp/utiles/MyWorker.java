//package com.george.ktorapp.utiles;
//
//import android.content.Context;
//import android.util.Log;
//
//import androidx.work.Worker;
//import androidx.work.WorkerParameters;
//
//public class MyWorker extends Worker {
//
//    private static final String TAG = "MyWorker";
//
//    public MyWorker(Context appContext, WorkerParameters workerParams) {
//        super(appContext, workerParams);
//    }
//
//    @Override
//    public Result doWork() {
//        Log.d(TAG, "Performing long running task in scheduled job");
//        // TODO(developer): add long running task here.
//        return Result.success();
//    }
//}
