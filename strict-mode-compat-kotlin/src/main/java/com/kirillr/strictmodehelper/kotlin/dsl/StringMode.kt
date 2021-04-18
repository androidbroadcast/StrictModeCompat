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

@Suppress("unused")
inline fun initStrictMode(
    enable: Boolean = true,
    enableDefaults: Boolean,
    config: (StrictModeConfig.() -> Unit)
) {
    initStrictMode(enable, config) {
        StrictModeConfig(enableDefaults)
    }
}

@Suppress("unused")
inline fun initStrictMode(
    enable: Boolean = true,
    defaultsThreadPolicy: StrictModeConfig.Defaults = StrictModeConfig.Defaults.DEFAULT,
    defaultsVmPolicy: StrictModeConfig.Defaults = StrictModeConfig.Defaults.DEFAULT,
    config: (StrictModeConfig.() -> Unit)
) {
    initStrictMode(enable, config) {
        StrictModeConfig(defaultsThreadPolicy, defaultsVmPolicy)
    }
}

@PublishedApi
internal inline fun initStrictMode(
    enable: Boolean,
    config: (StrictModeConfig.() -> Unit),
    createStrictModeConfig: () -> StrictModeConfig,
) {
    if (!enable) return

    with(createStrictModeConfig().apply(config)) {
        StrictMode.setThreadPolicy(buildThreadPolicy(threadPolicyConfig))
        StrictMode.setVmPolicy(buildVmPolicy(vmPolicyConfig))
    }
}
