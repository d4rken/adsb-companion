package eu.darken.adsbc.common.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import eu.darken.adsbc.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.adsbc.common.debug.logging.log

fun SharedPreferences.clearAndNotify() {
    val currentKeys = this.all.keys.toSet()
    log(VERBOSE) { "$this clearAndNotify(): $currentKeys" }
    edit {
        currentKeys.forEach { remove(it) }
    }
    // Clear does not notify anyone using registerOnSharedPreferenceChangeListener
    edit(commit = true) { clear() }
}
