package eu.darken.adsbc.feeder.ui.list.actions

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.flow.DynamicStateFlow
import eu.darken.adsbc.common.livedata.SingleLiveEvent
import eu.darken.adsbc.common.navigation.navArgs
import eu.darken.adsbc.common.ui.Confirmable
import eu.darken.adsbc.common.uix.ViewModel3
import eu.darken.adsbc.feeder.core.FeederId
import eu.darken.adsbc.feeder.core.FeederRepo
import eu.darken.adsbc.feeder.core.FeederUpdateService
import eu.darken.adsbc.feeder.core.feeder
import eu.darken.adsbc.feeder.ui.list.actions.FeederAction.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class FeederActionDialogVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val feederRepo: FeederRepo,
    private val feederUpdateService: FeederUpdateService,
) : ViewModel3(dispatcherProvider) {

    private val navArgs by handle.navArgs<FeederActionDialogArgs>()
    private val feederId: FeederId = navArgs.feederId
    private val stateUpdater = DynamicStateFlow(TAG, vmScope) { State(loading = true) }

    val renameFeederEvent = SingleLiveEvent<FeederId>()

    val state = stateUpdater.asLiveData2()

    init {
        launch {
            val feeder = feederRepo.feeder(feederId).first()

            val actions = listOf(
                Confirmable(REFRESH) { feederAction(it) },
                Confirmable(RENAME) { feederAction(it) },
                Confirmable(DELETE, requiredLvl = 1) { feederAction(it) },
            )

            stateUpdater.updateBlocking {
                if (feeder == null) {
                    copy(loading = true, finished = true)
                } else {
                    copy(
                        label = feeder.label,
                        loading = false,
                        allowedActions = actions
                    )
                }
            }
        }
    }

    private fun feederAction(action: FeederAction) = launch {
        stateUpdater.updateBlocking { copy(loading = true) }

        when (action) {
            RENAME -> {
                delay(200)
                renameFeederEvent.postValue(feederId)
            }
            REFRESH -> {
                try {
                    feederUpdateService.updateFeeder(feederId)
                } finally {
                    stateUpdater.updateBlocking { copy(loading = false, finished = true) }
                }
            }
            DELETE -> {
                delay(200)
                feederRepo.delete(feederId)
                stateUpdater.updateBlocking { copy(loading = false, finished = true) }
            }
        }
    }

    fun renameFeeder(id: FeederId, label: String?) = launch {
        log(TAG) { "renameFeeder(id=$id, label=$label)" }

        feederRepo.update(id) { it.copy(label = label) }
        stateUpdater.updateBlocking { copy(loading = false, finished = true) }
    }

    data class State(
        val loading: Boolean = false,
        val finished: Boolean = false,
        val label: String = "",
        val allowedActions: List<Confirmable<FeederAction>> = listOf()
    )

    companion object {
        private val TAG = logTag("Feeder", "ActionDialog", "VDC")
    }

}

