package eu.darken.adsbc.aircraft.core

import java.time.Instant

data class BaseAircraft(
    override val hexCode: String,
    override val lastSeenAt: Instant,
    override val reception: Float,
    override val totalMessages: Int,
    override val messageRate: Float,
    override val callsign: String? = null,
    override val squawkCode: String? = null,
    override val altitudeMeter: Long? = null,
    override val airSpeedKmh: Int? = null,
    override val distanceKmh: Float? = null
) : Aircraft