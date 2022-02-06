package eu.darken.adsbc.stats.core.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.darken.adsbc.feeder.core.FeederId
import java.time.Instant

@Entity(tableName = "stats_feeder")
data class FeederStatsData(
    @PrimaryKey val uid: FeederId,

    @ColumnInfo(name = "statsUpdatedAt") val statsUpdatedAt: Instant = Instant.MIN,
    @ColumnInfo(name = "aircraftCountLogged") val aircraftCountLogged: Int = 0,
)