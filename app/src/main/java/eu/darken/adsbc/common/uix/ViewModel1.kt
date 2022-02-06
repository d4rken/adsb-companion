package eu.darken.adsbc.common.uix

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag

abstract class ViewModel1 : ViewModel() {
    internal val tag: String = logTag("VM", javaClass.simpleName)

    init {
        log(tag) { "Initialized" }
    }

    @CallSuper
    override fun onCleared() {
        log(tag) { "onCleared()" }
        super.onCleared()
    }
}