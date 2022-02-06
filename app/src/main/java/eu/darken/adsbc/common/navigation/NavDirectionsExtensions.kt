package eu.darken.adsbc.common.navigation

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import eu.darken.adsbc.common.livedata.SingleLiveEvent

fun NavDirections.navVia(pub: MutableLiveData<in NavDirections>) = pub.postValue(this)

fun NavDirections.navVia(provider: NavEventSource) = this.navVia(provider.navEvents)

interface NavEventSource {
    val navEvents: SingleLiveEvent<in NavDirections>
}