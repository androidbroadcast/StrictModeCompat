/*
 * Copyright 2017-2022 Kirill Rozov
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

@StrictModeDsl
class StrictModeConfig @PublishedApi internal constructor(
    defaultsThreadPolicy: Defaults = Defaults.DEFAULT,
    defaultsVmPolicy: Defaults = Defaults.DEFAULT
) {

    constructor(enableDefaults: Boolean) : this(
        if (enableDefaults) Defaults.DEFAULT else Defaults.NONE,
        if (enableDefaults) Defaults.DEFAULT else Defaults.NONE,
    )

    @PublishedApi
    internal val threadPolicyConfig = ThreadPolicyConfig(defaultsThreadPolicy)

    @PublishedApi
    internal val vmPolicyConfig = VmPolicyConfig(defaultsVmPolicy)

    fun threadPolicy(config: @StrictModeDsl ThreadPolicyConfig.() -> Unit) {
        threadPolicyConfig.apply(config)
    }

    fun vmPolicy(config: @StrictModeDsl VmPolicyConfig.() -> Unit) {
        vmPolicyConfig.apply(config)
    }

    enum class Defaults {
        NONE, DEFAULT, ALL
    }
}