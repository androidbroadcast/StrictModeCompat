package com.kirillr.strictmodehelper.kotlin.dsl

import android.os.StrictMode
import com.kirillr.strictmodehelper.StrictModeCompat

@Suppress("unused")
fun initStrictMode(enableDefaults: Boolean = true, config: (StrictModeConfig.() -> Unit)) {
    StrictModeConfig(enableDefaults).apply {
        config()
        buildThreadPolicy(threadPolicyConfig)?.let(StrictMode::setThreadPolicy)
        buildVmPolicy(vmPolicyConfig)?.let(StrictMode::setVmPolicy)
    }
}

private fun buildThreadPolicy(config: ThreadPolicyConfig?): StrictMode.ThreadPolicy? {
    config ?: return null

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
    }
    return threadPolicyBuilder.build()
}

private fun buildVmPolicy(config: VmPolicyConfig?): StrictMode.VmPolicy? {
    config ?: return null

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

    config.classesInstanceLimit.toMap().forEach { (clazz, limit) ->
        vmPolicyBuilder.setClassInstanceLimit(clazz.java, limit)
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
    }

    return vmPolicyBuilder.build()
}