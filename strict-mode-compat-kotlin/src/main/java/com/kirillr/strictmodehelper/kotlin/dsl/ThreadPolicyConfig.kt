package com.kirillr.strictmodehelper.kotlin.dsl

class ThreadPolicyConfig internal constructor(private val enableDefaults: Boolean) {

    var customSlowCalls = if (enableDefaults) DEFAULT_CUSTOM_SLOW_CALLS else false
    var diskReads = if (enableDefaults) DEFAULT_DISK_READS else false
    var diskWrites = if (enableDefaults) DEFAULT_DISK_WRITES else false
    var network = if (enableDefaults) DEFAULT_NETWORK else false
    var resourceMismatches = if (enableDefaults) DEFAULT_RESOURCE_MISMATCHES else false
    var unbufferedIo = if (enableDefaults) DEFAULT_UNBUFFERED_IO else false

    internal var penaltyConfig: PenaltyConfig? = null
        private set

    fun penalty(config: (@StrictModeDsl PenaltyConfig.() -> Unit)) {
        this.penaltyConfig = PenaltyConfig(enableDefaults).apply(config)
    }

    private companion object {

        private const val DEFAULT_CUSTOM_SLOW_CALLS = true
        private const val DEFAULT_DISK_READS = true
        private const val DEFAULT_DISK_WRITES = true
        private const val DEFAULT_NETWORK = true
        private const val DEFAULT_RESOURCE_MISMATCHES = true
        private const val DEFAULT_UNBUFFERED_IO = true
    }

    class PenaltyConfig internal constructor(enableDefaults: Boolean) {
        var death = if (enableDefaults) DEFAULT_DEATH else false
        var deathOnNetwork = if (enableDefaults) DEFAULT_DEATH_ON_NETWORK else false
        var dialog = if (enableDefaults) DEFAULT_DIALOG else false
        var dropBox = if (enableDefaults) DEFAULT_DROPBOX else false
        var flashScreen = if (enableDefaults) DEFAULT_FLASH_SCREEN else false
        var log = if (enableDefaults) DEFAULT_LOG else false

        private companion object {

            private const val DEFAULT_DEATH = false
            private const val DEFAULT_DEATH_ON_NETWORK = false
            private const val DEFAULT_DIALOG = false
            private const val DEFAULT_DROPBOX = false
            private const val DEFAULT_FLASH_SCREEN = false
            private const val DEFAULT_LOG = true
        }
    }
}