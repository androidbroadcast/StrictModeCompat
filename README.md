[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Android%20StrictMode%20Compat-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/5655)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.kirich1409/strict-mode-compat/badge.svg)](https://maven-badges.herokuapp.com/maven-central//com.github.kirich1409/strict-mode-compat)

Android StrictMode Compat
=========================

Wrapper of [StrictMode API](https://developer.android.com/reference/android/os/StrictMode.html) that can be safely called on any version of Android.
You must apply version of library for you project base on compileSdkVersion:

```groovy
android {
    compileSdkVersion 30 // 30 is required minimum
}

dependencies {    
    implementation "com.kirich1409:strict-mode-compat:30.2.0"

    // Kotlin Extensions
    implementation "com.kirich1409:strict-mode-compat-ktx:30.2.0"
}
```

Sample
------

###### build.gradle ######
```groovy
android {
    buildTypes {
        debug {
            buildConfigField 'boolean', 'DEVELOPER_MODE', 'true'
        }
        
        release {
            buildConfigField 'boolean', 'DEVELOPER_MODE', 'false'
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

Or you can use Kotlin extension and configure StrictModeCompat via DSL
###### SampleApplication.kt ######
```kotlin
class SampleApplicationKt : Application() {

    override fun onCreate() {
        super.onCreate()
        initStrictMode(enable = BuildConfig.DEVELOPER_MODE, enableDefaults = false) {
            threadPolicy {
                resourceMismatches = true
                customSlowCalls = true
                unbufferedIo = true

                penalty {
                    log = true
                }
            }

            vmPolicy {
                fileUriExposure = true
                leakedRegistrationObjects = true
                cleartextNetwork = true
                cleartextNetwork = true
                untaggedSockets = true
                contentUriWithoutPermission = true

                penalty {
                    log = true
                }
            }
        }
    }
}
```

License
-------

    Copyright 2017-2021 Kirill Rozov

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
