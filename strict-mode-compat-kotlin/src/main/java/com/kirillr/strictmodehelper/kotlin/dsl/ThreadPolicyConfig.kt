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

@file:Suppress("unused")

package com.kirillr.strictmodehelper.kotlin.dsl

import android.os.strictmode.Violation
import java.util.concurrent.Executor
import android.os.DropBoxManager
import android.os.StrictMode
import android.os.Build

@ThreadPolicyDsl
class ThreadPolicyConfig private constructor(
    /**
     * Enable detection of slow calls.
     */
    var customSlowCalls: Boolean,

    /**
     * Enable detection of disk reads.
     */
    var diskReads: Boolean,

    /**
     * Enable detection of disk writes.
     */
    var diskWrites: Boolean,

    /**
     * Enable detection of network operations.
     */
    var network: Boolean,

    /**
     * Enables detection of mismatches between defined resource types and getter calls.
     */
    var resourceMismatches: Boolean,

    /**
     * Detect unbuffered input/output operations.
     */
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
        /**
         * Crash the whole process on violation. This penalty runs at the end of all enabled penalties so you'll
         * still get see logging or other violations before the process dies.
         *
         * Unlike [deathOnNetwork], this applies to disk reads, disk writes, and network usage if their
         * corresponding detect flags are set.
         */
        var death: Boolean,

        /**
         * Crash the whole process on any network usage. Unlike [death], this penalty runs
         * *before* anything else. You must still have enable [network].
         */
        var deathOnNetwork: Boolean,

        /**
         * Show an annoying dialog to the developer on detected violations, rate-limited to be only a little annoying.
         */
        var dialog: Boolean,

        /**
         * Enable detected violations log a stacktrace and timing data to the [DropBox][DropBoxManager]
         * on policy violation. Intended mostly for platform integrators doing beta user field data collection.
         */
        var dropBox: Boolean,

        /**
         * Flash the screen during a violation.
         */
        var flashScreen: Boolean,

        /**
         * Log detected violations to the system log.
         */
        var log: Boolean
    ) {

        internal var onViolation: ((violation: Violation) -> Unit)? = null
        internal var onViolationExecutor: Executor? = null

        /**
         * Call [StrictMode.OnThreadViolationListener.onThreadViolation] on specified [executor] every violation.
         *
         * Work on [Build.VERSION_CODES.P] and newer.
         */
        fun onViolation(executor: Executor, body: (violation: Violation) -> Unit) {
            onViolationExecutor = executor
            this.onViolation = body
        }

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
