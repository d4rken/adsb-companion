package eu.darken.adsbc.feeder.ui.list.actions

import android.view.ViewGroup
import eu.darken.adsbc.common.lists.differ.setupDiffer
import eu.darken.adsbc.common.lists.modular.mods.DataBinderMod
import eu.darken.adsbc.common.ui.Confirmable
import eu.darken.adsbc.common.ui.ConfirmableActionAdapterVH
import eu.darken.bb.common.lists.differ.AsyncDiffer
import eu.darken.bb.common.lists.differ.HasAsyncDiffer
import eu.darken.bb.common.lists.modular.ModularAdapter
import eu.darken.bb.common.lists.modular.mods.SimpleVHCreatorMod
import javax.inject.Inject

class ActionsAdapter @Inject constructor() :
    ModularAdapter<ActionsAdapter.VH>(),
    HasAsyncDiffer<Confirmable<FeederAction>> {

    override val asyncDiffer: AsyncDiffer<ActionsAdapter, Confirmable<FeederAction>> = setupDiffer()

    override fun getItemCount(): Int = data.size

    init {
        modules.add(DataBinderMod(data))
        modules.add(SimpleVHCreatorMod { VH(it) })
    }

    class VH(parent: ViewGroup) : ConfirmableActionAdapterVH<FeederAction>(parent) {
        override fun getIcon(item: FeederAction): Int = item.iconRes

        override fun getLabel(item: FeederAction): String = getString(item.labelRes)
    }
}