package eu.darken.adsbc.main.core

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.adsbc.common.debug.autoreport.DebugSettings
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.preferences.PreferenceStoreMapper
import eu.darken.adsbc.common.preferences.Settings
import eu.darken.adsbc.common.preferences.createFlowPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeneralSettings @Inject constructor(
    @ApplicationContext private val context: Context,
    private val debugSettings: DebugSettings,
) : Settings() {

    override val preferences: SharedPreferences = context.getSharedPreferences("settings_core", Context.MODE_PRIVATE)

    val isBugTrackingEnabled = preferences.createFlowPreference("core.bugtracking.enabled", true)

    val updateInterval = preferences.createFlowPreference("core.update.interval", 10000L)

    override val preferenceDataStore: PreferenceDataStore = PreferenceStoreMapper(
        isBugTrackingEnabled,
        updateInterval,
        debugSettings.isAutoReportingEnabled,
        debugSettings.isDebugModeEnabled
    )

    companion object {
        internal val TAG = logTag("General", "Settings")
    }
}