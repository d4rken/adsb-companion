package eu.darken.adsbc.feeder.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun FeederRepo.feeder(id: FeederId): Flow<Feeder?> = allFeeders.map { fs ->
    fs.singleOrNull { it.uid == id }
}