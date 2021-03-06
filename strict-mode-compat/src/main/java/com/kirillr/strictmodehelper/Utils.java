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

package com.kirillr.strictmodehelper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

import com.kirillr.strictmodehelper.StrictModeCompat;

import java.util.Locale;

@RestrictTo(RestrictTo.Scope.LIBRARY)
final class Utils {

    private Utils() {
    }

    private final static String TAG = "StrictModeCompat";
    private final static String FEATURE_NOT_SUPPORTED_MSG = "%s:%s is not supported";

    static void logUnsupportedFeature(@NonNull String category, @NonNull String feature) {
        Log.d(TAG, String.format(Locale.US, FEATURE_NOT_SUPPORTED_MSG, category, feature));
    }
}
