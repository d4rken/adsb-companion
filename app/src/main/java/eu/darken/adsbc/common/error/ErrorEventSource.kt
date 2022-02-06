package eu.darken.adsbc.common.error

import eu.darken.adsbc.common.livedata.SingleLiveEvent

interface ErrorEventSource {
    val errorEvents: SingleLiveEvent<Throwable>
}