package eu.darken.adsbc.feeder.core

import eu.darken.adsbc.aircraft.core.Aircraft
import eu.darken.adsbc.aircraft.core.AircraftStorage
import eu.darken.adsbc.common.collections.mutate
import eu.darken.adsbc.common.coroutine.AppScope
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.flow.DynamicStateFlow
import eu.darken.adsbc.common.flow.replayingShare
import eu.darken.adsbc.feeder.core.storage.FeederData
import eu.darken.adsbc.feeder.core.storage.FeederStorage
import eu.darken.adsbc.stats.core.api.ADSBxStatsApi
import eu.darken.adsbc.stats.core.storage.FeederStatsData
import eu.darken.adsbc.stats.core.storage.StatsStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeederRepo @Inject constructor(
    @AppScope private val appScope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
    private val feederStorage: FeederStorage,
    private val statsStorage: StatsStorage,
    private val aircraftStorage: AircraftStorage,
) {
    private val feederErrors = DynamicStateFlow<Map<FeederId, Throwable>>(
        loggingTag = TAG,
        parentScope = appScope + dispatcherProvider.IO
    ) { emptyMap() }

    val allFeeders: Flow<Collection<Feeder>> = combine(
        feederStorage.allFeeders,
        feederErrors.flow,
        aircraftStorage.allAircraft,
        statsStorage.allFeederStats
    ) { feeders, errorMap, aircraftMap, feederStats ->

        feeders.map { feederData ->
            val feederId = feederData.uid
            Feeder(
                data = feederData,
                stats = feederStats.singleOrNull { it.uid == feederId } ?: FeederStatsData(uid = feederId),
                aircraft = aircraftMap[feederId] ?: emptySet(),
                lastError = errorMap[feederId]
            )
        }
    }.replayingShare(appScope)

    val mergedAircrafts: Flow<Collection<SpottedAircraft>> = allFeeders.map { feeders ->
        val aircraft = feeders.map { it.aircraft }.flatten().distinctBy { it.hexCode }

        aircraft.map { ac ->
            SpottedAircraft(
                aircraft = ac,
                feeders = feeders
                    .filter { feeder ->
                        feeder.aircraft.any { it.hexCode == ac.hexCode }
                    }
                    .map { feeder ->
                        val specificAircraft = feeders
                            .single { it.uid == feeder.uid }
                            .aircraft.first { it.hexCode == ac.hexCode }
                        feeder to specificAircraft
                    }
            )
        }
    }.replayingShare(appScope)

    suspend fun update(feederId: FeederId, block: (FeederData) -> FeederData) {
        log(TAG) { "update(feederId=$feederId, block=$block)" }
        feederStorage.update(feederId, block)
    }

    suspend fun updateAircraft(feederId: FeederId, aircraft: Collection<Aircraft>) {
        log(TAG) { "updateStats(feederId=$feederId, aircraft=${aircraft.size})" }
        val start = System.currentTimeMillis()

        aircraftStorage.updateData(feederId, aircraft)

        feederStorage.update(feederId) { it.copy(seenAt = Instant.now()) }

        val duration = (System.currentTimeMillis() - start)
        log(TAG, VERBOSE) { "Aircraft update took $duration ms" }
    }

    suspend fun updateStats(feederId: FeederId, stats: ADSBxStatsApi.FeederInfo) {
        log(TAG) { "updateStats(feederId=$feederId, stats=$stats)" }
        val start = System.currentTimeMillis()

        statsStorage.update(feederId) {
            it.copy(
                statsUpdatedAt = Instant.now(),
                aircraftCountLogged = stats.aircraftCountLogged
            )
        }
        feederStorage.update(feederId) { it.copy(seenAt = Instant.now()) }

        val duration = (System.currentTimeMillis() - start)
        log(TAG, VERBOSE) { "Stats update took $duration ms" }
    }

    suspend fun updateError(feederId: FeederId, e: Throwable) {
        log(TAG) { "updateError(id=$feederId, error=$e)" }
        feederErrors.updateBlocking {
            mutate { this[feederId] = e }
        }
    }

    suspend fun delete(feederId: FeederId) {
        log(TAG) { "delete(id=$feederId)" }
        feederStorage.delete(feederId)
        statsStorage.delete(feederId)
        aircraftStorage.deleteByFeederId(feederId)
    }

    suspend fun createFeeder(request: FeederSetupRequest): Feeder {
        log(TAG) { "createFeeder(request=$request)" }
        val newData = feederStorage.createFeeder(request)
        statsStorage.create(newData.uid)

        return allFeeders
            .map { fs -> fs.firstOrNull { it.uid == newData.uid } }
            .filterNotNull()
            .first()
            .also { log(TAG) { "Feeder created: $it" } }
    }

    companion object {
        private val TAG = logTag("Feeder", "Repo")
    }
}