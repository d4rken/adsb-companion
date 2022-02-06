package eu.darken.adsbc.feeder.core

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.preferences.PreferenceStoreMapper
import eu.darken.adsbc.common.preferences.Settings
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeederSettings @Inject constructor(
    @ApplicationContext private val context: Context
) : Settings() {

    override val preferences: SharedPreferences = context.getSharedPreferences("settings_feeder", Context.MODE_PRIVATE)

    override val preferenceDataStore: PreferenceDataStore = PreferenceStoreMapper(

    )

    companion object {
        internal val TAG = logTag("Feeder", "Settings")
    }
}