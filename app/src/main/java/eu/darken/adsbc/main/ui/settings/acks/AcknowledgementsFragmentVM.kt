package eu.darken.adsbc.main.ui.settings.acks

import androidx.lifecycle.SavedStateHandle
import dagger.assisted.AssistedInject
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.uix.ViewModel3

class AcknowledgementsFragmentVM @AssistedInject constructor(
    private val handle: SavedStateHandle,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel3(dispatcherProvider) {

    companion object {
        private val TAG = logTag("Settings", "Acknowledgements", "VM")
    }
}