package eu.darken.adsbc.stats.core.storage

import eu.darken.adsbc.feeder.core.FeederId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun StatsStorage.byFeederId(feederId: FeederId): Flow<FeederStatsData?> = allFeederStats.map { fs ->
    fs.singleOrNull { it.uid == feederId }
}