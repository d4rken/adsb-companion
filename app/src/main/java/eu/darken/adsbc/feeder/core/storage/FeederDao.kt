package eu.darken.adsbc.feeder.core.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.darken.adsbc.feeder.core.ADSBxId
import eu.darken.adsbc.feeder.core.FeederId
import kotlinx.coroutines.flow.Flow

@Dao
interface FeederDao {

    @Query("SELECT * FROM feeders")
    fun allFeeders(): Flow<List<FeederData>>

    @Query("SELECT * FROM feeders WHERE adsbxId IN (:ids)")
    fun getFeederByAdsbxId(vararg ids: ADSBxId): FeederData?

    @Query("SELECT * FROM feeders WHERE uid IN (:ids)")
    fun getFeederById(vararg ids: FeederId): FeederData?

    @Insert
    suspend fun insertFeeder(feeder: FeederData)

    @Update(entity = FeederData::class)
    suspend fun updateFeeder(feeder: FeederData)

    @Query("DELETE FROM feeders WHERE uid = :id")
    suspend fun deleteFeeder(id: FeederId)
}