package com.kirillr.application;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.kirillr.strictmodehelper.StrictModeCompat;

import java.util.concurrent.Executors;

public class SampleApplication extends Application {

    private static final String TAG = "Sample";

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEVELOPER_MODE) {
            StrictMode.ThreadPolicy threadPolicy = new StrictModeCompat.ThreadPolicy.Builder()
                    .detectAll()
                    .detectCustomSlowCalls()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .detectResourceMismatches()
                    .detectUnbufferedIo()
                    .penaltyDeath()
                    .penaltyDeathOnNetwork()
                    .penaltyDialog()
                    .penaltyDropBox()
                    .penaltyFlashScreen()
                    .penaltyListener(Executors.newSingleThreadExecutor(), violation -> Log.e(TAG, "", violation))
                    .penaltyLog()
                    .permitAll()
                    .permitCustomSlowCalls()
                    .permitDiskReads()
                    .permitDiskWrites()
                    .permitNetwork()
                    .permitResourceMismatches()
                    .permitUnbufferedIo()
                    .build();

            StrictMode.VmPolicy vmPolicy = new StrictModeCompat.VmPolicy.Builder()
                    .detectActivityLeaks()
                    .detectAll()
                    .detectCleartextNetwork()
                    .detectContentUriWithoutPermission()
                    .detectFileUriExposure()
                    .detectLeakedClosableObjects()
                    .detectLeakedRegistrationObjects()
                    .detectLeakedSqlLiteObjects()
                    .detectNonSdkApiUsage()
                    .detectUntaggedSockets()
                    .penaltyDeath()
                    .penaltyDeathOnCleartextNetwork()
                    .penaltyDeathOnFileUriExposure()
                    .penaltyDropBox()
                    .penaltyListener(Executors.newSingleThreadExecutor(), violation -> Log.e(TAG, "", violation))
                    .penaltyLog()
                    .permitNonSdkApiUsage()
                    .setClassInstanceLimit(Object.class, 100)
                    .build();

            StrictModeCompat.setPolicies(threadPolicy, vmPolicy);
        }
    }
}
