/*
 * Copyright 2017-2019 Kirill Rozov
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

package com.kirillr.strictmodehelper.kotlin.dsl

import android.os.StrictMode
import android.os.strictmode.Violation
import com.kirillr.strictmodehelper.StrictModeCompat

@Suppress("unused")
fun initStrictMode(
    enable: Boolean = true,
    enableDefaults: Boolean = true,
    config: (StrictModeConfig.() -> Unit)
) {
    if (!enable) return

    StrictModeConfig(enableDefaults).apply {
        config()
        StrictMode.setThreadPolicy(
            buildThreadPolicy(threadPolicyConfig ?: ThreadPolicyConfig(enableDefaults))
        )
        StrictMode.setVmPolicy(
            buildVmPolicy(vmPolicyConfig ?: VmPolicyConfig(enableDefaults))
        )
    }
}

private fun buildThreadPolicy(config: ThreadPolicyConfig): StrictMode.ThreadPolicy {
    val threadPolicyBuilder = StrictModeCompat.ThreadPolicy.Builder()
    if (config.customSlowCalls) {
        threadPolicyBuilder.detectCustomSlowCalls()
    }
    if (config.diskReads) {
        threadPolicyBuilder.detectDiskReads()
    }
    if (config.diskWrites) {
        threadPolicyBuilder.detectDiskWrites()
    }
    if (config.network) {
        threadPolicyBuilder.detectNetwork()
    }
    if (config.resourceMismatches) {
        threadPolicyBuilder.detectResourceMismatches()
    }
    if (config.unbufferedIo) {
        threadPolicyBuilder.detectUnbufferedIo()
    }

    config.penaltyConfig.let { penaltyConfig ->
        if (penaltyConfig.death) {
            threadPolicyBuilder.penaltyDeath()
        }
        if (penaltyConfig.deathOnNetwork) {
            threadPolicyBuilder.penaltyDeathOnNetwork()
        }
        if (penaltyConfig.dialog) {
            threadPolicyBuilder.penaltyDialog()
        }
        if (penaltyConfig.dropBox) {
            threadPolicyBuilder.penaltyDropBox()
        }
        if (penaltyConfig.flashScreen) {
            threadPolicyBuilder.penaltyFlashScreen()
        }
        if (penaltyConfig.log) {
            threadPolicyBuilder.penaltyLog()
        }

        val onViolation = penaltyConfig.onViolation
        if (onViolation != null) {
            threadPolicyBuilder.penaltyListener(checkNotNull(penaltyConfig.onViolationExecutor),
                StrictModeCompat.OnThreadViolationListener { violation -> onViolation(violation) })
        }
    }
    return threadPolicyBuilder.build()
}

private fun buildVmPolicy(config: VmPolicyConfig): StrictMode.VmPolicy {
    val vmPolicyBuilder = StrictModeCompat.VmPolicy.Builder()
    if (config.activityLeaks) {
        vmPolicyBuilder.detectActivityLeaks()
    }
    if (config.cleartextNetwork) {
        vmPolicyBuilder.detectCleartextNetwork()
    }
    if (config.contentUriWithoutPermission) {
        vmPolicyBuilder.detectContentUriWithoutPermission()
    }
    if (config.fileUriExposure) {
        vmPolicyBuilder.detectFileUriExposure()
    }
    if (config.leakedClosableObjects) {
        vmPolicyBuilder.detectLeakedClosableObjects()
    }
    if (config.leakedRegistrationObjects) {
        vmPolicyBuilder.detectLeakedRegistrationObjects()
    }
    if (config.leakedSqlLiteObjects) {
        vmPolicyBuilder.detectLeakedSqlLiteObjects()
    }
    if (config.nonSdkApiUsage) {
        vmPolicyBuilder.detectNonSdkApiUsage()
    }
    if (config.untaggedSockets) {
        vmPolicyBuilder.detectUntaggedSockets()
    }
    if (config.credentialProtectedWhileLocked) {
        vmPolicyBuilder.detectCredentialProtectedWhileLocked()
    }
    if (config.implicitDirectBoot) {
        vmPolicyBuilder.detectImplicitDirectBoot()
    }

    config.classesInstanceLimit.apply {
        if (isNotEmpty()) {
            toMap().forEach { (clazz, limit) ->
                vmPolicyBuilder.setClassInstanceLimit(clazz.java, limit)
            }
        }
    }

    config.penaltyConfig.let { penaltyConfig ->
        if (penaltyConfig.death) {
            vmPolicyBuilder.penaltyDeath()
        }
        if (penaltyConfig.deathOnCleartextNetwork) {
            vmPolicyBuilder.penaltyDeathOnCleartextNetwork()
        }
        if (penaltyConfig.deathOnFileUriExposure) {
            vmPolicyBuilder.penaltyDeathOnFileUriExposure()
        }
        if (penaltyConfig.dropBox) {
            vmPolicyBuilder.penaltyDropBox()
        }
        if (penaltyConfig.log) {
            vmPolicyBuilder.penaltyLog()
        }

        val onViolation = penaltyConfig.onViolation
        if (onViolation != null) {
            vmPolicyBuilder.penaltyListener(checkNotNull(penaltyConfig.onViolationExecutor),
                StrictModeCompat.OnVmViolationListener { violation -> onViolation(violation) })
        }
    }

    return vmPolicyBuilder.build()
}
