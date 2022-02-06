package eu.darken.adsbc.feeder.ui.list.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.adsbc.R
import eu.darken.adsbc.common.lists.differ.update
import eu.darken.adsbc.common.uix.BottomSheetDialogFragment2
import eu.darken.adsbc.databinding.FeederListActionDialogBinding
import eu.darken.bb.common.lists.setupDefaults
import javax.inject.Inject

@AndroidEntryPoint
class FeederActionDialog : BottomSheetDialogFragment2() {
    override val vm: FeederActionDialogVM by viewModels()
    override lateinit var ui: FeederListActionDialogBinding
    @Inject lateinit var actionsAdapter: ActionsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        ui = FeederListActionDialogBinding.inflate(inflater, container, false)
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

        vm.renameFeederEvent.observe2(ui) { feederId ->
            MaterialAlertDialogBuilder(requireContext()).apply {
                val alertLayout = layoutInflater.inflate(R.layout.feeder_list_rename_dialog, null)
                val input: EditText = alertLayout.findViewById(R.id.input_text)
                setView(alertLayout)
                setTitle(R.string.rename_feeder_title)
                setMessage(R.string.rename_feeder_description)
                setPositiveButton(R.string.feeder_rename_action) { _, _ ->
                    vm.renameFeeder(feederId, input.text.toString())
                }
                setNegativeButton(R.string.general_cancel_action) { _, _ -> }
                setNeutralButton(R.string.general_reset_action) { _, _ ->
                    vm.renameFeeder(feederId, null)
                }
            }.show()
        }

        super.onViewCreated(view, savedInstanceState)
    }
}