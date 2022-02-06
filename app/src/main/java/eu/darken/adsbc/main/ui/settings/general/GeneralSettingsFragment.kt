package eu.darken.adsbc.main.ui.settings.general

import androidx.annotation.Keep
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.adsbc.R
import eu.darken.adsbc.common.debug.logging.Logging.Priority.ERROR
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.uix.PreferenceFragment2
import eu.darken.adsbc.databinding.SettingsNumberInputDialogBinding
import eu.darken.adsbc.main.core.GeneralSettings
import javax.inject.Inject

@Keep
@AndroidEntryPoint
class GeneralSettingsFragment : PreferenceFragment2() {

    private val vdc: GeneralSettingsFragmentVM by viewModels()

    @Inject lateinit var generalSettings: GeneralSettings

    override val settings: GeneralSettings by lazy { generalSettings }
    override val preferenceFile: Int = R.xml.preferences_general

    override fun onPreferenceTreeClick(preference: Preference): Boolean = when (preference.key) {
        "feeder.update.interval" -> showFeederUpdateIntervalDialog(preference)
        else -> super.onPreferenceTreeClick(preference)
    }

    private fun showFeederUpdateIntervalDialog(preference: Preference): Boolean {
        MaterialAlertDialogBuilder(requireContext()).apply {
            val layout = SettingsNumberInputDialogBinding.inflate(layoutInflater)

            layout.inputText.setText(generalSettings.updateInterval.value.let { it / 1000 }.toString())
            layout.inputLayout.hint = "Seconds"

            setView(layout.root)
            setTitle("Feeder update interval")
            setMessage("Time in seconds how often each feeder should be polled.")
            setPositiveButton(R.string.general_set_action) { _, _ ->
                val textToParse = layout.inputText.text.toString()
                try {
                    generalSettings.updateInterval.value = textToParse.toLong() * 1000L
                } catch (e: Exception) {
                    log(TAG, ERROR) { "Failed to update feeder update interval with $textToParse" }
                }
            }
            setNegativeButton(R.string.general_cancel_action) { _, _ -> }
            setNeutralButton(R.string.general_reset_action) { _, _ ->
                generalSettings.updateInterval.reset()
            }
        }.show()
        return true
    }

    companion object {
        private val TAG = logTag("Settings", "General")
    }
}