package com.kirillr.application;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

import com.kirillr.strictmodehelper.StrictModeCompat;

import java.util.concurrent.Executors;

public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEVELOPER_MODE) {
            StrictMode.ThreadPolicy threadPolicy = new StrictModeCompat.ThreadPolicy.Builder()
                    .detectResourceMismatches()
                    .detectCustomSlowCalls()
                    .detectUnbufferedIo()  // Available only on Android 8.0+
                    .penaltyLog()
                    .build();

            StrictMode.VmPolicy vmPolicy = new StrictModeCompat.VmPolicy.Builder()
                    .detectFileUriExposure()
                    .detectLeakedRegistrationObjects()
                    .detectCleartextNetwork()
                    .detectUntaggedSockets() // Available only on Android 8.0+
                    .detectContentUriWithoutPermission()  // Available only on Android 8.0+
                    .penaltyLog()
                    .build();

            StrictModeCompat.setPolicies(threadPolicy, vmPolicy);
        }
    }
}
