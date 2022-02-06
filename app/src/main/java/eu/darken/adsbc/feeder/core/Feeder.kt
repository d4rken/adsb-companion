package eu.darken.adsbc.feeder.core

import eu.darken.adsbc.aircraft.core.Aircraft
import eu.darken.adsbc.feeder.core.storage.FeederData
import eu.darken.adsbc.stats.core.storage.FeederStatsData
import java.time.Instant

data class Feeder(
    val data: FeederData,
    val stats: FeederStatsData,
    val aircraft: Collection<Aircraft> = emptySet(),
    val lastError: Throwable? = null,
) {
    val label: String
        get() = data.label?.ifBlank { null } ?: adsbxId.value

    val lastSeenAt: Instant
        get() = data.seenAt

    val uid: FeederId
        get() = data.uid

    val adsbxId: ADSBxId
        get() = data.adsbxId
}
