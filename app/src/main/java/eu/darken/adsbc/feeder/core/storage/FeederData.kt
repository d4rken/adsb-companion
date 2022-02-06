package eu.darken.adsbc.feeder.core.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import eu.darken.adsbc.feeder.core.ADSBxId
import eu.darken.adsbc.feeder.core.FeederId
import java.time.Instant

@Entity(tableName = "feeders")
data class FeederData(
    @PrimaryKey val uid: FeederId = FeederId(),
    @ColumnInfo(name = "addedAt") val addedAt: Instant = Instant.now(),
    @ColumnInfo(name = "label") val label: String? = null,
    @ColumnInfo(name = "comment") val comment: String? = null,

    @ColumnInfo(name = "adsbxId") val adsbxId: ADSBxId,

    @ColumnInfo(name = "seenAt") val seenAt: Instant = Instant.MIN,
)