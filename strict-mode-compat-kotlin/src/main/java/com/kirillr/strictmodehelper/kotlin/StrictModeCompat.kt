package com.kirillr.strictmodehelper.kotlin

/**
 * @authror Kirill Rozov
 * @date 29/7/17.
 */
object StrictModeCompat {

    @JvmStatic
    fun noteSlowCall(lazyMessage: () -> String) {
        com.kirillr.strictmodehelper.StrictModeCompat.noteSlowCall(lazyMessage())
    }
}