package eu.darken.adsbc.feeder.core

import eu.darken.adsbc.aircraft.core.Aircraft

data class SpottedAircraft(
    val aircraft: Aircraft,
    val feeders: Collection<Pair<Feeder, Aircraft>>
)
