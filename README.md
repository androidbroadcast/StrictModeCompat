Android StrictMode Compat
=========================

Wrapper of [StrictMode API](https://developer.android.com/reference/android/os/StrictMode.html) that can be safely called on any version of Android.
You must apply version of library for you project base on compileSdkVersion:

```groovy
android {
    compileSdkVersion 25
    …
}

dependencies {
    compile "com.kirich1409:strict-mode-compat:25.0.0"
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
```

Download
--------

```groovy
compile 'com.kirich1409:strict-mode-compat:25.0.0'
```

License
-------

    Copyright 2017 Kirill Rozov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
