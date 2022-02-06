package eu.darken.adsbc.feeder.ui.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.adsbc.R
import eu.darken.adsbc.common.WebpageTool
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.common.lists.differ.update
import eu.darken.adsbc.common.uix.Fragment3
import eu.darken.adsbc.common.viewbinding.viewBinding
import eu.darken.adsbc.databinding.FeederListFragmentBinding
import eu.darken.adsbc.databinding.FeederListInputDialogBinding
import eu.darken.bb.common.lists.setupDefaults
import javax.inject.Inject


@AndroidEntryPoint
class FeederListFragment : Fragment3(R.layout.feeder_list_fragment) {

    override val vm: FeederListFragmentVM by viewModels()
    override val ui: FeederListFragmentBinding by viewBinding()

    @Inject lateinit var webpageTool: WebpageTool
    @Inject lateinit var feederAdapter: FeederAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.list.setupDefaults(feederAdapter)

        ui.fab.setOnClickListener {
            log(TAG) { "Launching add feeder dialog" }

            MaterialAlertDialogBuilder(requireContext()).apply {
                val layout = FeederListInputDialogBinding.inflate(layoutInflater)

                setView(layout.root)
                setTitle(R.string.add_feeder_title)
                setMessage(R.string.add_feeder_description)
                setPositiveButton(R.string.general_add_action) { _, _ ->
                    vm.addFeeder(
                        label = layout.feederLabelInputText.text.toString(),
                        adsbxId = layout.adsbxIdInputText.text.toString()
                    )
                }
                setNegativeButton(R.string.general_cancel_action) { _, _ -> }
                setNeutralButton(R.string.general_help_info) { _, _ ->
                    webpageTool.open("https://github.com/adsbxchange/adsbexchange-stats")
                }
            }.show()
        }

        vm.feeders.observe2(ui) {
            feederAdapter.update(it)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private val TAG = logTag("Feeder", "List", "Fragment")
    }
}
