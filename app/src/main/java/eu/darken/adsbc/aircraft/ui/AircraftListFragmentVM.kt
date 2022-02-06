package eu.darken.adsbc.aircraft.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.uix.ViewModel3
import eu.darken.adsbc.feeder.core.FeederRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import java.time.Instant
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class AircraftListFragmentVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val feederRepo: FeederRepo,
) : ViewModel3(dispatcherProvider = dispatcherProvider) {

    private val ticker = flow {
        while (coroutineContext.isActive) {
            emit(Instant.now())
            delay(1000)
        }
    }
    val aircraft: LiveData<List<AircraftAdapter.Item>> = combine(
        feederRepo.mergedAircrafts,
        ticker
    ) { aircraft, now ->
        aircraft.map { spot ->
            AircraftAdapter.Item(
                spottedAircraft = spot,
            ) { log(TAG) { "Aircraft clicked: $it" } }
        }
            .sortedByDescending { it.spottedAircraft.feeders.size }
    }.asLiveData2()

    companion object {
        private val TAG = logTag("Aircraft", "List", "VM")
    }
}
