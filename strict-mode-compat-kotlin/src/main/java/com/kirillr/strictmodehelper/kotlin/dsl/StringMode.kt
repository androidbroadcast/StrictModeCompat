/*
 * Copyright 2017-2021 Kirill Rozov
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
import com.kirillr.strictmodehelper.StrictModeCompat

@Suppress("unused")
fun initStrictMode(
    enable: Boolean = true,
    enableDefaults: Boolean = true,
    config: (StrictModeConfig.() -> Unit)
) {
    if (!enable) return

    with(StrictModeConfig(enableDefaults).apply(config)) {
        threadPolicyConfig?.let(::buildThreadPolicy).let(StrictMode::setThreadPolicy)
        vmPolicyConfig?.let(::buildVmPolicy).let(StrictMode::setVmPolicy)
    }
}

private fun buildThreadPolicy(config: ThreadPolicyConfig): StrictMode.ThreadPolicy {
    return StrictModeCompat.ThreadPolicy.Builder().apply {
        with(config) {
            if (customSlowCalls) detectCustomSlowCalls()
            if (diskReads) detectDiskReads()
            if (diskWrites) detectDiskWrites()
            if (network) detectNetwork()
            if (resourceMismatches) detectResourceMismatches()
            if (unbufferedIo) detectUnbufferedIo()
        }

        with(config.penaltyConfig) {
            if (death) penaltyDeath()
            if (deathOnNetwork) penaltyDeathOnNetwork()
            if (dialog) penaltyDialog()
            if (dropBox) penaltyDropBox()
            if (flashScreen) penaltyFlashScreen()
            if (log) penaltyLog()

            onViolation?.let { onViolation ->
                penaltyListener(checkNotNull(onViolationExecutor), onViolation)
            }
        }
    }.build()
}

private fun buildVmPolicy(config: VmPolicyConfig): StrictMode.VmPolicy {
    return StrictModeCompat.VmPolicy.Builder().apply {
        with(config) {
            if (activityLeaks) detectActivityLeaks()
            if (cleartextNetwork) detectCleartextNetwork()
            if (contentUriWithoutPermission) detectContentUriWithoutPermission()
            if (fileUriExposure) detectFileUriExposure()
            if (leakedClosableObjects) detectLeakedClosableObjects()
            if (leakedRegistrationObjects) detectLeakedRegistrationObjects()
            if (leakedSqlLiteObjects) detectLeakedSqlLiteObjects()
            if (nonSdkApiUsage) detectNonSdkApiUsage()
            if (untaggedSockets) detectUntaggedSockets()
            if (credentialProtectedWhileLocked) detectCredentialProtectedWhileLocked()
            if (implicitDirectBoot) detectImplicitDirectBoot()

            classesInstanceLimit.apply {
                if (isNotEmpty()) {
                    toMap().forEach { (clazz, limit) ->
                        setClassInstanceLimit(clazz.java, limit)
                    }
                }
            }
        }

        with(config.penaltyConfig) {
            if (death) penaltyDeath()
            if (deathOnCleartextNetwork) penaltyDeathOnCleartextNetwork()
            if (deathOnFileUriExposure) penaltyDeathOnFileUriExposure()
            if (dropBox) penaltyDropBox()
            if (log) penaltyLog()

            onViolation?.let { onViolation ->
                penaltyListener(checkNotNull(onViolationExecutor), onViolation)
            }
        }
    }.build()
}
