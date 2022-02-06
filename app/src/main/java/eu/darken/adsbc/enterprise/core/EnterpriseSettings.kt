package eu.darken.adsbc.enterprise.core

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
class EnterpriseSettings @Inject constructor(
    @ApplicationContext private val context: Context
) : Settings() {

    override val preferences: SharedPreferences =
        context.getSharedPreferences("settings_enterprise", Context.MODE_PRIVATE)

    val isEnterpriseMode = preferences.createFlowPreference("enterprise.enabled", false)

    override val preferenceDataStore: PreferenceDataStore = PreferenceStoreMapper(
        isEnterpriseMode
    )

    companion object {
        internal val TAG = logTag("Enterprise", "Settings")
    }
}