[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20StrictMode%20Compat-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5655)
[![Download](https://api.bintray.com/packages/kirich1409/maven/strict-mode-compat/images/download.svg)](https://bintray.com/kirich1409/maven/strict-mode-compat/_latestVersion)

Android StrictMode Compat
=========================

Wrapper of [StrictMode API](https://developer.android.com/reference/android/os/StrictMode.html) that can be safely called on any version of Android.
You must apply version of library for you project base on compileSdkVersion:

```groovy
android {
    compileSdkVersion 28
    …
}

dependencies {    
    // With AndroidX
    implementation "com.kirich1409:strict-mode-compat:28.1.0"
    
    // Without AndroidX
    implementation "com.kirich1409:strict-mode-compat:28.0.0"
}
```

Sample
------

###### build.gradle ######
```groovy
android {
    defaultConfig {
        buildConfigField 'boolean', 'DEVELOPER_MODE', 'false'
    }

    buildTypes {
        debug {
            buildConfigField 'boolean', 'DEVELOPER_MODE', 'true'
        }
    }
}
```

###### SampleApplication.java ######
```java
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
```

Download
--------

```groovy
compile 'com.kirich1409:strict-mode-compat:28.1.0'
```

Kotlin Extensions
-----------------

For project that using Kotlin you can add

```groovy
compile 'com.kirich1409:strict-mode-compat-kotlin:28.1.0'
```

instead of dependency described in [Download](#Download)

License
-------

    Copyright 2017-2019 Kirill Rozov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
