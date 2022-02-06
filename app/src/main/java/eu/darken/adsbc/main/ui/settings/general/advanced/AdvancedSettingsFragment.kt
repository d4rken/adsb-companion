package eu.darken.adsbc.main.ui.settings.general.advanced

import androidx.annotation.Keep
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.adsbc.R
import eu.darken.adsbc.common.uix.PreferenceFragment2
import eu.darken.adsbc.main.core.GeneralSettings
import javax.inject.Inject

@Keep
@AndroidEntryPoint
class AdvancedSettingsFragment : PreferenceFragment2() {

    private val vdc: AdvancedSettingsFragmentVM by viewModels()

    @Inject lateinit var debugSettings: GeneralSettings

    override val settings: GeneralSettings by lazy { debugSettings }
    override val preferenceFile: Int = R.xml.preferences_advanced

}