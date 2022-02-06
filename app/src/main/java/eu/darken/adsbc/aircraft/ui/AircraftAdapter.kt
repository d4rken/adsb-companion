package eu.darken.adsbc.aircraft.ui

import android.annotation.SuppressLint
import android.icu.text.RelativeDateTimeFormatter
import android.view.ViewGroup
import eu.darken.adsbc.R
import eu.darken.adsbc.aircraft.core.Aircraft
import eu.darken.adsbc.common.lists.BindableVH
import eu.darken.adsbc.common.lists.differ.DifferItem
import eu.darken.adsbc.common.lists.differ.setupDiffer
import eu.darken.adsbc.common.lists.modular.mods.DataBinderMod
import eu.darken.adsbc.databinding.AircraftListLineBinding
import eu.darken.adsbc.feeder.core.SpottedAircraft
import eu.darken.bb.common.lists.differ.AsyncDiffer
import eu.darken.bb.common.lists.differ.HasAsyncDiffer
import eu.darken.bb.common.lists.modular.ModularAdapter
import eu.darken.bb.common.lists.modular.mods.SimpleVHCreatorMod
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

class AircraftAdapter @Inject constructor() : ModularAdapter<AircraftAdapter.AircraftVH>(),
    HasAsyncDiffer<AircraftAdapter.Item> {

    override val asyncDiffer: AsyncDiffer<*, Item> = setupDiffer()

    override fun getItemCount(): Int = data.size

    init {
        modules.add(DataBinderMod(data))
        modules.add(SimpleVHCreatorMod { AircraftVH(it) })
    }

    data class Item(
        val spottedAircraft: SpottedAircraft,
        val onClickAction: (Aircraft) -> Unit
    ) : DifferItem {
        override val stableId: Long = spottedAircraft.aircraft.hexCode.hashCode().toLong()
    }

    class AircraftVH(parent: ViewGroup) : ModularAdapter.VH(R.layout.aircraft_list_line, parent),
        BindableVH<Item, AircraftListLineBinding> {

        override val viewBinding: Lazy<AircraftListLineBinding> = lazy { AircraftListLineBinding.bind(itemView) }

        private val lastSeenFormatter = RelativeDateTimeFormatter.getInstance()

        @SuppressLint("SetTextI18n")
        override val onBindData: AircraftListLineBinding.(
            item: Item,
            payloads: List<Any>
        ) -> Unit = { item, _ ->
            val ac = item.spottedAircraft.aircraft
            label.text = "${ac.callsign} (${ac.hexCode})"

            val timeAgo = Duration.between(
                ac.lastSeenAt,
                Instant.now()
            )

            lastSeen.text = lastSeenFormatter.format(
                timeAgo.seconds.toDouble(),
                RelativeDateTimeFormatter.Direction.LAST,
                RelativeDateTimeFormatter.RelativeUnit.SECONDS
            )

            val seenBy = item.spottedAircraft.feeders
                .sortedByDescending { it.second.reception }
                .joinToString(", ") {
                    "${it.first.label} (${it.second.reception} dBm)"
                }
            receptionStats.text = seenBy
            aircraftStats.text = "SQAWK ${ac.squawkCode}"

            itemView.setOnClickListener { item.onClickAction(ac) }
        }
    }
}