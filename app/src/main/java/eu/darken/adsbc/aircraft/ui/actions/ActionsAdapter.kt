package eu.darken.adsbc.aircraft.ui.actions

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
    HasAsyncDiffer<Confirmable<AircraftAction>> {

    override val asyncDiffer: AsyncDiffer<ActionsAdapter, Confirmable<AircraftAction>> = setupDiffer()

    override fun getItemCount(): Int = data.size

    init {
        modules.add(DataBinderMod(data))
        modules.add(SimpleVHCreatorMod { VH(it) })
    }

    class VH(parent: ViewGroup) : ConfirmableActionAdapterVH<AircraftAction>(parent) {
        override fun getIcon(item: AircraftAction): Int = item.iconRes

        override fun getLabel(item: AircraftAction): String = getString(item.labelRes)

    }
}