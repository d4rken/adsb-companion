package eu.darken.adsbc.common.uix

import androidx.navigation.NavDirections
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.asLog
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.error.ErrorEventSource
import eu.darken.adsbc.common.flow.setupCommonEventHandlers
import eu.darken.adsbc.common.livedata.SingleLiveEvent
import eu.darken.adsbc.common.navigation.NavEventSource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn


abstract class ViewModel3(
    dispatcherProvider: DispatcherProvider,
) : ViewModel2(dispatcherProvider), NavEventSource, ErrorEventSource {

    override val navEvents = SingleLiveEvent<NavDirections?>()
    override val errorEvents = SingleLiveEvent<Throwable>()

    init {
        launchErrorHandler = CoroutineExceptionHandler { _, ex ->
            log(tag) { "Error during launch: ${ex.asLog()}" }
            errorEvents.postValue(ex)
        }
    }

    override fun <T> Flow<T>.launchInViewModel() = this
        .setupCommonEventHandlers(tag) { "launchInViewModel()" }
        .launchIn(vmScope)
}