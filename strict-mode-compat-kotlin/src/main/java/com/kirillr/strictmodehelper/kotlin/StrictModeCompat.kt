package com.kirillr.strictmodehelper.kotlin

import com.kirillr.strictmodehelper.StrictModeCompat

/**
 * Kotlin extensions for {@link com.kirillr.strictmodehelper.StrictModeCompat}
 *
 * @authror Kirill Rozov
 * @date 29/7/17.
 */
object StrictModeCompat {

    /**
     * For code to note that it's slow.  This is a no-op unless the
     * current thread's {@link android.os.StrictMode.ThreadPolicy} has
     * {@link android.os.StrictMode.ThreadPolicy.Builder#detectCustomSlowCalls}
     * enabled.
     *
     * @param lazyMessage Short string for the exception stack trace that's
     *                    built if when this fires.
     *
     * @see StrictModeCompat#noteSlowCall(String, Object...)
     * @see StrictModeCompat#noteSlowCall(Locale, String, Object...)
     */
    @JvmStatic
    fun noteSlowCall(lazyMessage: () -> String) {
        com.kirillr.strictmodehelper.StrictModeCompat.noteSlowCall(lazyMessage())
    }
}