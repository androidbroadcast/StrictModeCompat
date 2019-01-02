package com.kirillr.strictmodehelper.kotlin.dsl

@DslMarker
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.SOURCE)
internal annotation class StrictModeDsl
