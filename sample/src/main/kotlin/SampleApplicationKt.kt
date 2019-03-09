import android.annotation.SuppressLint
import android.app.Application
import android.os.StrictMode
import android.util.Log

import com.kirillr.application.BuildConfig
import com.kirillr.strictmodehelper.StrictModeCompat
import com.kirillr.strictmodehelper.kotlin.dsl.initStrictMode

import java.util.concurrent.Executors

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
