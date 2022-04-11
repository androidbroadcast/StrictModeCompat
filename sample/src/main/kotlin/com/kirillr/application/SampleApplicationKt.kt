package com.kirillr.application/*
 * Copyright 2017-2022 Kirill Rozov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint
import android.app.Application
import com.kirillr.application.BuildConfig
import com.kirillr.strictmodehelper.kotlin.dsl.StrictModeConfig
import com.kirillr.strictmodehelper.kotlin.dsl.initStrictMode

@SuppressLint("Registered")
class SampleApplicationKt : Application() {

    override fun onCreate() {
        super.onCreate()
        initStrictMode(
            enable = BuildConfig.DEVELOPER_MODE,
            defaultsThreadPolicy = StrictModeConfig.Defaults.DEFAULT,
            defaultsVmPolicy = StrictModeConfig.Defaults.DEFAULT,
        ) {
            threadPolicy {
                resourceMismatches = true
                customSlowCalls = true
                unbufferedIo = true
                diskWrites = true

                penalty {
                    log = true
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
