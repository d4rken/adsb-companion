package eu.darken.adsbc.aircraft.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.adsbc.R
import eu.darken.adsbc.common.WebpageTool
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.lists.differ.update
import eu.darken.adsbc.common.uix.Fragment3
import eu.darken.adsbc.common.viewbinding.viewBinding
import eu.darken.adsbc.databinding.AircraftListFragmentBinding
import eu.darken.bb.common.lists.setupDefaults
import javax.inject.Inject

@AndroidEntryPoint
class AircraftListFragment : Fragment3(R.layout.aircraft_list_fragment) {

    override val vm: AircraftListFragmentVM by viewModels()
    override val ui: AircraftListFragmentBinding by viewBinding()

    @Inject lateinit var webpageTool: WebpageTool
    @Inject lateinit var aircraftAdapter: AircraftAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.list.setupDefaults(aircraftAdapter)

        vm.aircraft.observe2(ui) { aircraftAdapter.update(it) }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private val TAG = logTag("Aircraft", "List", "Fragment")
    }
}
