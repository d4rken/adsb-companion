package eu.darken.adsbc.stats.core.storage

import androidx.room.withTransaction
import eu.darken.adsbc.common.debug.logging.Logging.Priority.ERROR
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.feeder.core.FeederId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsStorage @Inject constructor(
    private val statsDatabaseFactory: StatsDatabase.Factory,
) {

    private val db by lazy { statsDatabaseFactory.create() }
    private val statsDao by lazy { db.statsDao() }

    val allFeederStats = statsDao.allFeederStats()

    suspend fun create(id: FeederId, stats: FeederStatsData? = null): FeederStatsData {
        log(TAG) { "create(id=$id, stats=$stats)" }

        return db.withTransaction {
            val existing = statsDao.getFeederStatsByFeederId(id)
            if (existing != null) {
                log(TAG, ERROR) { "Stats already exist: $id" }
                throw IllegalStateException("$id already exists")
            }

            val newStats = FeederStatsData(uid = id)

            statsDao.insertStats(newStats)
            statsDao.getFeederStatsByFeederId(id)!!.also {
                log(TAG) { "Stats created: $it" }
            }
        }
    }

    suspend fun delete(feederId: FeederId) {
        log(TAG) { "delete(feederId=$feederId)" }
        statsDao.deleteStats(feederId)
    }

    suspend fun update(id: FeederId, block: (FeederStatsData) -> FeederStatsData) {
        log(TAG) { "update(id=$id, block=$block)" }

        db.withTransaction {
            val oldFeeder = statsDao.getFeederStatsByFeederId(id) ?: return@withTransaction
            val updatedStats = block(oldFeeder)
            log(TAG) { "Updated feeder stats for $id\nBefore:$oldFeeder\nNow:   $oldFeeder" }
            statsDao.updateStats(updatedStats)
        }
    }

    companion object {
        private val TAG = logTag("Stats", "Storage")
    }
}