package com.kirillr.application;

import android.app.Application;
import android.os.StrictMode;

import com.kirillr.strictmodehelper.StrictModeCompat;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEVELOPER_MODE) {
            StrictMode.ThreadPolicy threadPolicy = new StrictModeCompat.ThreadPolicy.Builder()
                    .detectResourceMismatches()
                    .detectCustomSlowCalls()
                    .penaltyLog()
                    .build();

            StrictMode.VmPolicy vmPolicy = new StrictModeCompat.VmPolicy.Builder()
                    .detectFileUriExposure()
                    .detectLeakedRegistrationObjects()
                    .detectCleartextNetwork()
                    .penaltyLog()
                    .build();

            StrictModeCompat.setPolicies(threadPolicy, vmPolicy);
        }
    }
}
