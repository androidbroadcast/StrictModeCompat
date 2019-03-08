package com.kirillr.strictmodehelper.kotlin.dsl

@StrictModeDsl
class StrictModeConfig internal constructor(enableDefaults: Boolean) {

    internal var threadPolicyConfig = ThreadPolicyConfig(enableDefaults)
        private set

    internal var vmPolicyConfig = VmPolicyConfig(enableDefaults)
        private set

    fun threadPolicy(config: (@StrictModeDsl ThreadPolicyConfig.() -> Unit)) {
        threadPolicyConfig.apply(config)
    }

    fun vmPolicy(config: (@StrictModeDsl VmPolicyConfig.() -> Unit)) {
        vmPolicyConfig.apply(config)
    }
}