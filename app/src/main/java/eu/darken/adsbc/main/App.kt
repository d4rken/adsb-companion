package eu.darken.adsbc.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import eu.darken.adsbc.BuildConfig
import eu.darken.adsbc.common.coroutine.AppScope
import eu.darken.adsbc.common.debug.autoreport.AutoReporting
import eu.darken.adsbc.common.debug.logging.*
import eu.darken.adsbc.feeder.core.FeederUpdateService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var bugReporter: AutoReporting
    @Inject lateinit var feederUpdateService: FeederUpdateService
    @AppScope @Inject lateinit var appScope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Logging.install(LogCatLogger())

//        ReLinker
//            .log { message -> log(TAG) { "ReLinker: $message" } }
//            .loadLibrary(this, "bugsnag-plugin-android-anr")

        bugReporter.setup()

        feederUpdateService
            .autoUpdates
            .launchIn(appScope)

        log(TAG) { "onCreate() done! ${Exception().asLog()}" }
    }

    companion object {
        internal val TAG = logTag("ADSBC")
    }
}
