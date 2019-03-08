package com.kirillr.strictmodehelper.kotlin.dsl

import kotlin.reflect.KClass

@VmPolicyDsl
class VmPolicyConfig internal constructor(enableDefaults: Boolean) {

    var activityLeaks = if (enableDefaults) DEFAULT_ACTIVITY_LEAKS else false
    var cleartextNetwork = if (enableDefaults) DEFAULT_CLEARTEXT_NETWORK else false
    var contentUriWithoutPermission = if (enableDefaults) DEFAULT_CONTENT_URI_WITHOUT_PERMISSION else false
    var fileUriExposure = if (enableDefaults) DEFAULT_FILE_URI_EXPOSURE else false
    var leakedClosableObjects = if (enableDefaults) DEFAULT_LEAKED_CLOSABLE_OBJECTS else false
    var leakedRegistrationObjects = if (enableDefaults) DEFAULT_LEAKED_REGISTRATION_OBJECTS else false
    var leakedSqlLiteObjects = if (enableDefaults) DEFAULT_LEAKED_SQL_LITE_OBJECTS else false
    var nonSdkApiUsage = if (enableDefaults) DEFAULT_NON_SDK_API_USAGE else false
    var untaggedSockets = if (enableDefaults) DEFAULT_UNTAGGED_SOCKETS else false

    var classesInstanceLimit = mapOf<KClass<*>, Int>()

    internal var penaltyConfig = PenaltyConfig(enableDefaults)
        private set

    fun penalty(config: (@VmPolicyDsl PenaltyConfig.() -> Unit)) {
        this.penaltyConfig.apply(config)
    }

    private companion object {

        private const val DEFAULT_ACTIVITY_LEAKS = true
        private const val DEFAULT_CLEARTEXT_NETWORK = true
        private const val DEFAULT_CONTENT_URI_WITHOUT_PERMISSION = true
        private const val DEFAULT_FILE_URI_EXPOSURE = true
        private const val DEFAULT_LEAKED_CLOSABLE_OBJECTS = true
        private const val DEFAULT_LEAKED_REGISTRATION_OBJECTS = true
        private const val DEFAULT_LEAKED_SQL_LITE_OBJECTS = true
        private const val DEFAULT_NON_SDK_API_USAGE = true
        private const val DEFAULT_UNTAGGED_SOCKETS = true
    }

    class PenaltyConfig private constructor(
        var death: Boolean,
        var deathOnCleartextNetwork: Boolean,
        var deathOnFileUriExposure: Boolean,
        var dropBox: Boolean,
        var log: Boolean
    ) {

        internal companion object {

            internal operator fun invoke(enableDefaults: Boolean): PenaltyConfig {
                return if (enableDefaults) default() else disableAll()
            }

            private fun disableAll(): PenaltyConfig {
                return PenaltyConfig(
                    death = false,
                    deathOnCleartextNetwork = false,
                    deathOnFileUriExposure = false,
                    dropBox = false,
                    log = false
                )
            }

            private fun default(): PenaltyConfig {
                return PenaltyConfig(
                    death = DEFAULT_DEATH,
                    deathOnCleartextNetwork = DEFAULT_DEATH_ON_CLEARTEXT_NETWORK,
                    deathOnFileUriExposure = DEFAULT_DEATH_ON_FILE_URI_EXPOSURE,
                    dropBox = DEFAULT_DROPBOX,
                    log = DEFAULT_LOG
                )
            }

            private const val DEFAULT_DEATH = false
            private const val DEFAULT_DEATH_ON_CLEARTEXT_NETWORK = false
            private const val DEFAULT_DEATH_ON_FILE_URI_EXPOSURE = false
            private const val DEFAULT_DROPBOX = false
            private const val DEFAULT_LOG = true
        }
    }
}