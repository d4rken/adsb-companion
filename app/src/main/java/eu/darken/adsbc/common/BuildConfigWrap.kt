package eu.darken.adsbc.common

import eu.darken.adsbc.BuildConfig


// Can't be const because that prevents them from being mocked in tests
@Suppress("MayBeConstant")
object BuildConfigWrap {
    val APPLICATION_ID = BuildConfig.APPLICATION_ID
    val DEBUG: Boolean = BuildConfig.DEBUG
    val BUILD_TYPE: String = BuildConfig.BUILD_TYPE

    val VERSION_CODE: Long = BuildConfig.VERSION_CODE.toLong()
    val VERSION_NAME: String = BuildConfig.VERSION_NAME
    val GIT_SHA: String = BuildConfig.GITSHA

    val VERSION_DESCRIPTION: String = "v$VERSION_NAME ($VERSION_CODE) [$GIT_SHA]"
}
