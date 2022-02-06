package eu.darken.adsbc.aircraft.ui.actions

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.adsbc.aircraft.ui.actions.AircraftAction.DETAILS
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.flow.DynamicStateFlow
import eu.darken.adsbc.common.navigation.navArgs
import eu.darken.adsbc.common.ui.Confirmable
import eu.darken.adsbc.common.uix.ViewModel3
import eu.darken.adsbc.stats.core.storage.StatsStorage
import javax.inject.Inject

@HiltViewModel
class AircraftActionDialogVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val feederStorage: StatsStorage,
) : ViewModel3(dispatcherProvider) {

    private val navArgs by handle.navArgs<AircraftActionDialogArgs>()
    private val aircraftId = navArgs.aircraftId
    private val stateUpdater = DynamicStateFlow(TAG, vmScope) { State(loading = true) }

    val state = stateUpdater.asLiveData2()

    init {
        launch {
//            val feeder = feederRepo.feeder(feederId).first()
//
//            val actions = listOf(
//                Confirmable(DETAILS) { aircraftAction(it) },
//            )
//
//            stateUpdater.updateBlocking {
//                if (feeder == null) {
//                    copy(loading = true, finished = true)
//                } else {
//                    copy(
//                        label = feeder.labelOrId,
//                        loading = false,
//                        allowedActions = actions
//                    )
//                }
//            }
        }
    }

    private fun aircraftAction(action: AircraftAction) = launch {
        stateUpdater.updateBlocking { copy(loading = true) }
        launch {
            try {
                when (action) {
                    DETAILS -> {

                    }
                }
            } finally {
                stateUpdater.updateBlocking { copy(loading = false, finished = true) }
            }
        }
    }

    data class State(
        val loading: Boolean = false,
        val finished: Boolean = false,
        val label: String = "",
        val allowedActions: List<Confirmable<AircraftAction>> = listOf()
    )

    companion object {
        private val TAG = logTag("Aircraft", "ActionDialog", "VM")
    }

}