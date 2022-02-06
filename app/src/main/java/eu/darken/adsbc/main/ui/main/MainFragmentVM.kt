package eu.darken.adsbc.main.ui.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.darken.adsbc.R
import eu.darken.adsbc.aircraft.ui.AircraftListFragment
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.uix.ViewModel3
import eu.darken.adsbc.enterprise.core.EnterpriseSettings
import eu.darken.adsbc.feeder.ui.list.FeederListFragment
import eu.darken.adsbc.main.core.GeneralSettings
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MainFragmentVM @Inject constructor(
    handle: SavedStateHandle,
    dispatcherProvider: DispatcherProvider,
    private val generalSettings: GeneralSettings,
    private val enterpriseSettings: EnterpriseSettings,
) : ViewModel3(dispatcherProvider = dispatcherProvider) {

    private var currentPosition: Int = 0

    data class State(
        val pages: List<MainPagerAdapter.Page>,
        val pagePosition: Int,
        val isEnterprise: Boolean,
    )

    val state = combine(
        generalSettings.isBugTrackingEnabled.flow,
        enterpriseSettings.isEnterpriseMode.flow
    ) { _, isEnterprise ->
        val basePages = listOf(
            MainPagerAdapter.Page(FeederListFragment::class, R.string.feeder_list_page_label),
            MainPagerAdapter.Page(AircraftListFragment::class, R.string.aircraft_list_page_label),
        )

        State(
            pages = basePages,
            pagePosition = currentPosition,
            isEnterprise = isEnterprise,
        )
    }.asLiveData2()

    fun updateCurrentPage(position: Int) {
        currentPosition = position
    }

    fun upgradeEnterprise() {
        enterpriseSettings.isEnterpriseMode.value = true
    }

}