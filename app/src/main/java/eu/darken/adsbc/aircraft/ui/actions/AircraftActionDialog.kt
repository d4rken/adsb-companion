package eu.darken.adsbc.aircraft.ui.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.adsbc.common.lists.differ.update
import eu.darken.adsbc.common.uix.BottomSheetDialogFragment2
import eu.darken.adsbc.databinding.AircraftListActionDialogBinding
import eu.darken.bb.common.lists.setupDefaults
import javax.inject.Inject

@AndroidEntryPoint
class AircraftActionDialog : BottomSheetDialogFragment2() {
    override val vm: AircraftActionDialogVM by viewModels()
    override lateinit var ui: AircraftListActionDialogBinding
    @Inject lateinit var actionsAdapter: ActionsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ui = AircraftListActionDialogBinding.inflate(inflater, container, false)
        return ui.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.recyclerview.setupDefaults(actionsAdapter)

        vm.state.observe2(ui) { state ->
            feederName.text = state.label

            actionsAdapter.update(state.allowedActions)

            ui.recyclerview.visibility = if (state.loading) View.INVISIBLE else View.VISIBLE
            ui.progressCircular.visibility = if (state.loading) View.VISIBLE else View.INVISIBLE
            if (state.finished) dismissAllowingStateLoss()
        }

        super.onViewCreated(view, savedInstanceState)
    }
}