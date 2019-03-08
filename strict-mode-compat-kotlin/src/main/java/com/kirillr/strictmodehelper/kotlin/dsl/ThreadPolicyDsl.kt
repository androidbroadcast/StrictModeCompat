package com.kirillr.strictmodehelper.kotlin.dsl

@DslMarker
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
internal annotation class ThreadPolicyDsl
