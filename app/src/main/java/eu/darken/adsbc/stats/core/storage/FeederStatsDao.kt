package eu.darken.adsbc.stats.core.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import eu.darken.adsbc.feeder.core.FeederId
import kotlinx.coroutines.flow.Flow

@Dao
interface FeederStatsDao {

    @Query("SELECT * FROM stats_feeder")
    fun allFeederStats(): Flow<List<FeederStatsData>>

    @Query("SELECT * FROM stats_feeder WHERE uid IN (:ids)")
    fun getFeederStatsByFeederId(vararg ids: FeederId): FeederStatsData?

    @Insert
    suspend fun insertStats(stats: FeederStatsData)

    @Update(entity = FeederStatsData::class)
    suspend fun updateStats(stats: FeederStatsData)

    @Query("DELETE FROM stats_feeder WHERE uid = :id")
    suspend fun deleteStats(id: FeederId)
}