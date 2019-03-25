

import android.annotation.SuppressLint
import android.app.Application
import com.kirillr.application.BuildConfig
import com.kirillr.strictmodehelper.kotlin.dsl.initStrictMode

@SuppressLint("Registered")
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
                    threadPolicy {
                        diskWrites = true
                    }
                }
            }

            vmPolicy {
                fileUriExposure = true
                leakedRegistrationObjects = true
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
