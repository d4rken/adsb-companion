package eu.darken.adsbc.feeder.core

import eu.darken.adsbc.aircraft.core.api.ADSBxAircraftEndpoint
import eu.darken.adsbc.common.debug.logging.Logging.Priority.*
import eu.darken.adsbc.common.debug.logging.asLog
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.main.core.GeneralSettings
import eu.darken.adsbc.stats.core.api.ADSBxStatsEndpoint
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import java.time.Duration
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeederUpdateService @Inject constructor(
    private val aircraftEndpointAdsbx: ADSBxAircraftEndpoint,
    private val statsEndpointAdsbx: ADSBxStatsEndpoint,
    private val generalSettings: GeneralSettings,
    private val feederRepo: FeederRepo,
) {

    val autoUpdates = feederRepo.allFeeders
        .distinctUntilChangedBy { feeders ->
            feeders.map { it.adsbxId }
        }
        .flatMapLatest { ids ->
            generalSettings.updateInterval.flow.map { it to ids }
        }
        .flatMapLatest { (updateInterval, feeders) ->
            flow {
                while (currentCoroutineContext().isActive) {
                    val updates = feeders.map {
                        try {
                            updateFeeder(it.uid, updateInterval)
                        } catch (e: Exception) {
                            log(TAG, WARN) { "Auto update failed for $it" }
                        }
                    }
                    emit(updates)
                    delay(updateInterval)
                }
            }
        }


    suspend fun updateFeeder(feederId: FeederId, updateInterval: Long = 0) = try {
        log(TAG) { "updateFeeder(feederId=$feederId)" }
        val updateStart = System.currentTimeMillis()

        val feeder = feederRepo.feeder(feederId).firstOrNull()
            ?: throw IllegalArgumentException("Couldn't find $feederId")

        kotlin.run {
            val aircraft = aircraftEndpointAdsbx.getAircraft(feeder.adsbxId)
            feederRepo.updateAircraft(feederId, aircraft)
        }

        kotlin.run {
            val timeSinceLastStats = Duration.between(feeder.stats.statsUpdatedAt, Instant.now())

            if (timeSinceLastStats.seconds < updateInterval * 6) return@run

            feederRepo.updateStats(feederId, statsEndpointAdsbx.getFeederStats(feeder.adsbxId))
        }

        val updateDuration = System.currentTimeMillis() - updateStart
        log(TAG, VERBOSE) { "Update took ${updateDuration}ms for $feederId" }
        Unit
    } catch (e: Exception) {
        log(TAG, ERROR) { "Failed to update feeder info for feederId: ${e.asLog()}" }
        feederRepo.updateError(feederId, e)
        throw e
    }

    companion object {
        private val TAG = logTag("Feeder", "UpdateService")
    }
}