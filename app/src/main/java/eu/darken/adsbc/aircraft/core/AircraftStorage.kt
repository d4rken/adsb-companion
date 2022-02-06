package eu.darken.adsbc.aircraft.core

import eu.darken.adsbc.common.collections.mutate
import eu.darken.adsbc.common.coroutine.AppScope
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.flow.DynamicStateFlow
import eu.darken.adsbc.feeder.core.FeederId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.plus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AircraftStorage @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) {
    private val data = DynamicStateFlow<Map<FeederId, Collection<Aircraft>>>(
        parentScope = appScope + dispatcherProvider.IO
    ) {
        emptyMap()
    }

    val allAircraft: Flow<Map<FeederId, Collection<Aircraft>>> = data.flow

    suspend fun updateData(feederId: FeederId, aircraft: Collection<Aircraft>) {
        log(TAG) { "Updating aircraft for $feederId: ${aircraft.size} items" }
        data.updateBlocking {
            mutate { this[feederId] = aircraft }
        }
    }

    suspend fun deleteByFeederId(feederId: FeederId) {
        log(TAG) { "Deleting all aircraft for $feederId" }
        data.updateBlocking {
            mutate { remove(feederId) }
        }
    }

    companion object {
        private val TAG = logTag("Aircraft", "Storage")
    }
}