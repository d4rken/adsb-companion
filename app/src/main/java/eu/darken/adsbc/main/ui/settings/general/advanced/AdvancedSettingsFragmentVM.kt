package eu.darken.adsbc.main.ui.settings.general.advanced

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.uix.ViewModel3
import javax.inject.Inject

@HiltViewModel
class AdvancedSettingsFragmentVM @Inject constructor(
    private val handle: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel3(dispatcherProvider) {

    companion object {
        private val TAG = logTag("Settings", "General", "Advanced", "VM")
    }
}