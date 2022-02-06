package eu.darken.adsbc.feeder.ui.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.Logging.Priority.ERROR
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.navigation.navVia
import eu.darken.adsbc.common.uix.ViewModel3
import eu.darken.adsbc.feeder.core.*
import eu.darken.adsbc.main.ui.main.MainFragmentDirections
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class FeederListFragmentVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val feederRepo: FeederRepo,
    private val feederUpdateService: FeederUpdateService,
) : ViewModel3(dispatcherProvider = dispatcherProvider) {

    private val ticker = flow<Unit> {
        while (coroutineContext.isActive) {
            emit(Unit)
            delay(1000)
        }
    }
    val feeders = combine(
        feederRepo.allFeeders,
        ticker
    ) { feeders, _ ->
        feeders.map { feeder ->
            FeederAdapter.Item(feeder = feeder) { editFeeder(it.uid) }
        }
    }.asLiveData2()

    fun addFeeder(label: String, adsbxId: String) = launch {
        log(TAG) { "addFeeder(label=$label, adsbxId=$adsbxId)" }
        val request = FeederSetupRequest(
            label = label,
            adsbxId = ADSBxId(adsbxId)
        )
        val createdFeeder = feederRepo.createFeeder(request)
        try {
            feederUpdateService.updateFeeder(createdFeeder.uid)
        } catch (e: Exception) {
            log(TAG, ERROR) { "Failed to do initial feeder update." }
        }
    }

    fun editFeeder(id: FeederId) {
        MainFragmentDirections.actionMainFragmentToFeederActionDialog(id).navVia(this)
    }

    companion object {
        private val TAG = logTag("Feeder", "List", "VM")
    }
}
