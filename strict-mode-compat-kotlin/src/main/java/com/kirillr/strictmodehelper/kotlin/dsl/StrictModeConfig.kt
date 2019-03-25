package com.kirillr.strictmodehelper.kotlin.dsl

@StrictModeDsl
class StrictModeConfig internal constructor(private val enableDefaults: Boolean) {

    internal var threadPolicyConfig: ThreadPolicyConfig? = null
    internal var vmPolicyConfig: VmPolicyConfig? = null

    fun threadPolicy(config: @StrictModeDsl ThreadPolicyConfig.() -> Unit) {
        threadPolicyConfig = ThreadPolicyConfig(enableDefaults).apply(config)
    }

    fun vmPolicy(config: @StrictModeDsl VmPolicyConfig.() -> Unit) {
        vmPolicyConfig = VmPolicyConfig(enableDefaults).apply(config)
    }
}