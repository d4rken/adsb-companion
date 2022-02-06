package eu.darken.adsbc.common.uix

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import eu.darken.adsbc.common.coroutine.DefaultDispatcherProvider
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.Logging.Priority.WARN
import eu.darken.adsbc.common.debug.logging.asLog
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.error.ErrorEventSource
import eu.darken.adsbc.common.flow.DynamicStateFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlin.coroutines.CoroutineContext


abstract class ViewModel2(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : ViewModel1() {

    val vmScope = viewModelScope + dispatcherProvider.Default

    var launchErrorHandler: CoroutineExceptionHandler? = null

    private fun getVMContext(): CoroutineContext {
        val dispatcher = dispatcherProvider.Default
        return getErrorHandler()?.let { dispatcher + it } ?: dispatcher
    }

    private fun getErrorHandler(): CoroutineExceptionHandler? {
        val handler = launchErrorHandler
        if (handler != null) return handler

        if (this is ErrorEventSource) {
            return CoroutineExceptionHandler { _, ex ->
                log(WARN) { "Error during launch: ${ex.asLog()}" }
                errorEvents.postValue(ex)
            }
        }

        return null
    }

    fun <T : Any> DynamicStateFlow<T>.asLiveData2() = flow.asLiveData2()

    fun <T> Flow<T>.asLiveData2() = this.asLiveData(context = getVMContext())

    fun launch(
        scope: CoroutineScope = viewModelScope,
        context: CoroutineContext = getVMContext(),
        block: suspend CoroutineScope.() -> Unit
    ) {
        try {
            scope.launch(context = context, block = block)
        } catch (e: CancellationException) {
            log(tag, WARN) { "launch()ed coroutine was canceled (scope=$scope): ${e.asLog()}" }
        }
    }

    open fun <T> Flow<T>.launchInViewModel() = this.launchIn(vmScope)

}