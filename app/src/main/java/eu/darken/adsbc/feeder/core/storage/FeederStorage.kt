package eu.darken.adsbc.feeder.core.storage

import androidx.room.withTransaction
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.feeder.core.FeederId
import eu.darken.adsbc.feeder.core.FeederSetupRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeederStorage @Inject constructor(
    private val feederDatabaseFactory: FeederDatabase.Factory,
) {

    private val db by lazy { feederDatabaseFactory.create() }
    private val feederDao by lazy { db.feederDao() }

    val allFeeders = feederDao.allFeeders()

    suspend fun createFeeder(request: FeederSetupRequest): FeederData {
        log(TAG) { "createFeeder(request=$request)" }

        return db.withTransaction {
            val existingFeeder = feederDao.getFeederByAdsbxId(request.adsbxId)
            if (existingFeeder != null) throw IllegalStateException("${request.adsbxId} already exists")

            val entity = FeederData(
                adsbxId = request.adsbxId,
                label = request.label?.ifBlank { null }
            )
            feederDao.insertFeeder(entity)

            feederDao.getFeederByAdsbxId(request.adsbxId)!!
        }
    }

    suspend fun delete(feederId: FeederId) {
        log(TAG) { "delete(feederId=$feederId)" }
        feederDao.deleteFeeder(feederId)
    }

    suspend fun update(id: FeederId, block: (FeederData) -> FeederData): FeederData {
        log(TAG) { "updateInfo(id=$id, block=$block)" }
        return db.withTransaction {
            val oldFeeder = feederDao.getFeederById(id) ?: throw IllegalArgumentException("Can't find $id")
            val updatedFeeder = block(oldFeeder)
            log(TAG) { "Updated feeder $id\nBefore:$oldFeeder\nNow:   $oldFeeder" }
            feederDao.updateFeeder(updatedFeeder)
            updatedFeeder
        }
    }

    companion object {
        private val TAG = logTag("Feeder", "Storage")
    }
}