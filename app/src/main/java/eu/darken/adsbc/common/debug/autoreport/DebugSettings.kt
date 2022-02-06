package eu.darken.adsbc.common.debug.autoreport

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.preferences.PreferenceStoreMapper
import eu.darken.adsbc.common.preferences.Settings
import eu.darken.adsbc.common.preferences.createFlowPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DebugSettings @Inject constructor(
    @ApplicationContext private val context: Context,
) : Settings() {

    override val preferences: SharedPreferences by lazy {
        context.getSharedPreferences("debug_settings", Context.MODE_PRIVATE)
    }

    val isAutoReportingEnabled = preferences.createFlowPreference("debug.bugreport.automatic.enabled", false)

    val isDebugModeEnabled = preferences.createFlowPreference("debug.mode.enabled", false)

    override val preferenceDataStore: PreferenceDataStore = PreferenceStoreMapper(
        isAutoReportingEnabled,
        isDebugModeEnabled
    )

    companion object {
        internal val TAG = logTag("General", "Settings")
    }
}