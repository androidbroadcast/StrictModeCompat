package com.kirillr.strictmodehelper.kotlin.dsl

@ThreadPolicyDsl
class ThreadPolicyConfig internal constructor(enableDefaults: Boolean) {

    var customSlowCalls = if (enableDefaults) DEFAULT_CUSTOM_SLOW_CALLS else false
    var diskReads = if (enableDefaults) DEFAULT_DISK_READS else false
    var diskWrites = if (enableDefaults) DEFAULT_DISK_WRITES else false
    var network = if (enableDefaults) DEFAULT_NETWORK else false
    var resourceMismatches = if (enableDefaults) DEFAULT_RESOURCE_MISMATCHES else false
    var unbufferedIo = if (enableDefaults) DEFAULT_UNBUFFERED_IO else false

    internal var penaltyConfig = PenaltyConfig(enableDefaults)
        private set

    fun penalty(config: (@ThreadPolicyDsl PenaltyConfig.() -> Unit)) {
        this.penaltyConfig.apply(config)
    }

    private companion object {

        private const val DEFAULT_CUSTOM_SLOW_CALLS = true
        private const val DEFAULT_DISK_READS = true
        private const val DEFAULT_DISK_WRITES = true
        private const val DEFAULT_NETWORK = true
        private const val DEFAULT_RESOURCE_MISMATCHES = true
        private const val DEFAULT_UNBUFFERED_IO = true
    }

    class PenaltyConfig private constructor(
        var death: Boolean,
        var deathOnNetwork: Boolean,
        var dialog: Boolean,
        var dropBox: Boolean,
        var flashScreen: Boolean,
        var log: Boolean
    ) {

        internal companion object {

            internal operator fun invoke(enableDefaults: Boolean): PenaltyConfig {
                return if (enableDefaults) default() else disableAll()
            }

            private fun disableAll(): PenaltyConfig {
                return PenaltyConfig(
                    death = false,
                    deathOnNetwork = false,
                    dialog = false,
                    dropBox = false,
                    flashScreen = false,
                    log = false
                )
            }

            private fun default(): PenaltyConfig {
                return PenaltyConfig(
                    death = DEFAULT_DEATH,
                    deathOnNetwork = DEFAULT_DEATH_ON_NETWORK,
                    dialog = DEFAULT_DIALOG,
                    dropBox = DEFAULT_DROPBOX,
                    flashScreen = DEFAULT_FLASH_SCREEN,
                    log = DEFAULT_LOG
                )
            }

            private const val DEFAULT_DEATH = false
            private const val DEFAULT_DEATH_ON_NETWORK = false
            private const val DEFAULT_DIALOG = false
            private const val DEFAULT_DROPBOX = false
            private const val DEFAULT_FLASH_SCREEN = false
            private const val DEFAULT_LOG = true
        }
    }
}