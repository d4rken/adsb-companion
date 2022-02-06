package eu.darken.adsbc.aircraft.core

import java.time.Instant

interface Aircraft {
    val hexCode: String
    val lastSeenAt: Instant
    val totalMessages: Int
    val reception: Float
    val messageRate: Float

    val callsign: String?

    val squawkCode: String?

    val altitudeMeter: Long?

    val airSpeedKmh: Int?

    val distanceKmh: Float?
}