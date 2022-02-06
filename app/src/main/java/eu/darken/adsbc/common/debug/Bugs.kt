package eu.darken.adsbc.common.debug

import com.bugsnag.android.Bugsnag
import eu.darken.adsbc.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.adsbc.common.debug.logging.Logging.Priority.WARN
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag

object Bugs {
    var ready = false
    fun report(exception: Exception) {
        log(TAG, VERBOSE) { "Reporting $exception" }
        if (!ready) {
            log(TAG, WARN) { "Bug tracking not initialized yet." }
            return
        }
        Bugsnag.notify(exception)
    }

    private val TAG = logTag("Debug", "Bugs")
}