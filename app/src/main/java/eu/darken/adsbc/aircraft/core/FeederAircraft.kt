package eu.darken.adsbc.aircraft.core

import eu.darken.adsbc.feeder.core.FeederId

data class FeederAircraft(
    val feederId: FeederId,
    val aircraft: Aircraft
)
