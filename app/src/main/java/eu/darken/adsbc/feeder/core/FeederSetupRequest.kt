package eu.darken.adsbc.feeder.core

data class FeederSetupRequest(
    val adsbxId: ADSBxId,
    val label: String? = null
)