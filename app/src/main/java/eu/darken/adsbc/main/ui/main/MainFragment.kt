package eu.darken.adsbc.main.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import eu.darken.adsbc.R
import eu.darken.adsbc.common.colorString
import eu.darken.adsbc.common.navigation.doNavigate
import eu.darken.adsbc.common.uix.Fragment3
import eu.darken.adsbc.common.viewbinding.viewBinding
import eu.darken.adsbc.databinding.MainFragmentBinding

@AndroidEntryPoint
class MainFragment : Fragment3(R.layout.main_fragment) {

    override val vm: MainFragmentVM by viewModels()
    override val ui: MainFragmentBinding by viewBinding()

    lateinit var adapter: MainPagerAdapter

    private var showEnterPriseUpgrade: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ui.toolbar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_settings -> {
                        doNavigate(MainFragmentDirections.actionExampleFragmentToSettingsContainerFragment())
                        true
                    }
                    R.id.action_upgrade -> {
                        showUpgradeDialog()
                        true
                    }
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }

        vm.state.observe2(ui) { state ->
            adapter = MainPagerAdapter(childFragmentManager, lifecycle, state.pages)

            viewpager.adapter = adapter
            tablayout.tabMode = TabLayout.MODE_SCROLLABLE
            // When smoothScroll is enabled and we navigate to an unloaded fragment, ??? happens we jump to the wrong position
            TabLayoutMediator(tablayout, viewpager, true, false) { tab, position ->
                tab.setText(adapter.pages[position].titleRes)
            }.attach()

            viewpager.setCurrentItem(state.pagePosition, false)

            toolbar.menu.findItem(R.id.action_upgrade).isVisible = !state.isEnterprise
            toolbar.updateTitle(state.isEnterprise)
        }

        ui.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                vm.updateCurrentPage(position)
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun Toolbar.updateTitle(isEnterprise: Boolean) {
        subtitle = if (isEnterprise) {
            colorString(context, R.color.enterprise, getString(R.string.enterprise_mode_label) + " (╯°□°)╯︵ ┻━┻")
        } else {
            null
        }

    }

    private fun showUpgradeDialog() {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(R.string.general_upgrade_action)
            setMessage(R.string.upgrade_enterprise_description)
            setPositiveButton(R.string.general_upgrade_action) { _, _ ->
                vm.upgradeEnterprise()
            }
            setNegativeButton(R.string.general_cancel_action) { _, _ -> }
        }.show()
    }
}
