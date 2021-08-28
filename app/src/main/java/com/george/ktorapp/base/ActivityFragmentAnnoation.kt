package com.george.ktorapp.base


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class ActivityFragmentAnnoation(val contentId: Int)