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

package com.kirillr.strictmodehelper.kotlin

import com.kirillr.strictmodehelper.StrictModeCompat

/**
 * Kotlin extensions for {@link com.kirillr.strictmodehelper.StrictModeCompat}
 *
 * @author Kirill Rozov
 * @date 29/7/17.
 */
@Suppress("unused")
class StrictModeCompat private constructor() {

    companion object {

        /**
         * For code to note that it's slow. This is a no-op unless the
         * current thread's [android.os.StrictMode.ThreadPolicy] has
         * [android.os.StrictMode.ThreadPolicy.Builder.detectCustomSlowCalls] enabled.
         *
         * @param lazyMessage Short string for the exception stack trace that's built if when this fires.
         *
         * @see StrictModeCompat.noteSlowCall
         */
        @JvmStatic
        inline fun noteSlowCall(lazyMessage: () -> String) {
            StrictModeCompat.noteSlowCall(lazyMessage())
        }
    }
}