package com.kirillr.strictmodehelper.kotlin.dsl

class StrictModeConfig internal constructor(private val enableDefaults: Boolean) {

    internal var threadPolicyConfig: ThreadPolicyConfig? = null
        private set

    internal var vmPolicyConfig: VmPolicyConfig? = null
        private set

    fun threadPolicy(config: (@StrictModeDsl ThreadPolicyConfig.() -> Unit)) {
        threadPolicyConfig = ThreadPolicyConfig(enableDefaults).apply(config)
    }

    fun vmPolicy(config: (@StrictModeDsl VmPolicyConfig.() -> Unit)) {
        vmPolicyConfig = VmPolicyConfig(enableDefaults).apply(config)
    }
}