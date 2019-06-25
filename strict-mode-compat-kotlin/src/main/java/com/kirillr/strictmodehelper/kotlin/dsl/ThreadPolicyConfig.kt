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

@ThreadPolicyDsl
class ThreadPolicyConfig private constructor(
    var customSlowCalls: Boolean,
    var diskReads: Boolean,
    var diskWrites: Boolean,
    var network: Boolean,
    var resourceMismatches: Boolean,
    var unbufferedIo: Boolean,
    internal val penaltyConfig: PenaltyConfig
) {

    fun penalty(config: @ThreadPolicyDsl PenaltyConfig.() -> Unit) {
        this.penaltyConfig.apply(config)
    }

    internal companion object {

        internal operator fun invoke(enableDefaults: Boolean): ThreadPolicyConfig {
            return if (enableDefaults) default() else disableAll()
        }

        private fun disableAll(): ThreadPolicyConfig {
            return ThreadPolicyConfig(
                customSlowCalls = false,
                diskReads = false,
                diskWrites = false,
                network = false,
                resourceMismatches = false,
                unbufferedIo = false,
                penaltyConfig = PenaltyConfig(false)
            )
        }

        private fun default(): ThreadPolicyConfig {
            return ThreadPolicyConfig(
                customSlowCalls = DEFAULT_CUSTOM_SLOW_CALLS,
                diskReads = DEFAULT_DISK_READS,
                diskWrites = DEFAULT_DISK_WRITES,
                network = DEFAULT_NETWORK,
                resourceMismatches = DEFAULT_RESOURCE_MISMATCHES,
                unbufferedIo = DEFAULT_UNBUFFERED_IO,
                penaltyConfig = PenaltyConfig(true)
            )
        }

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