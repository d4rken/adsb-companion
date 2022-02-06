package eu.darken.adsbc.feeder.ui.list

import android.text.format.DateUtils
import android.view.ViewGroup
import eu.darken.adsbc.R
import eu.darken.adsbc.common.lists.BindableVH
import eu.darken.adsbc.common.lists.binding
import eu.darken.adsbc.common.lists.differ.DifferItem
import eu.darken.adsbc.common.lists.differ.setupDiffer
import eu.darken.adsbc.common.lists.modular.mods.DataBinderMod
import eu.darken.adsbc.databinding.FeederListLineBinding
import eu.darken.adsbc.feeder.core.Feeder
import eu.darken.bb.common.lists.differ.AsyncDiffer
import eu.darken.bb.common.lists.differ.HasAsyncDiffer
import eu.darken.bb.common.lists.modular.ModularAdapter
import eu.darken.bb.common.lists.modular.mods.SimpleVHCreatorMod
import java.time.Instant
import javax.inject.Inject

class FeederAdapter @Inject constructor() : ModularAdapter<FeederAdapter.FeederVH>(),
    HasAsyncDiffer<FeederAdapter.Item> {

    override val asyncDiffer: AsyncDiffer<*, Item> = setupDiffer()

    override fun getItemCount(): Int = data.size

    init {
        modules.add(DataBinderMod(data))
        modules.add(SimpleVHCreatorMod { FeederVH(it) })
    }

    data class Item(
        val feeder: Feeder,
        val onClickAction: (Feeder) -> Unit
    ) : DifferItem {
        override val payloadProvider: ((DifferItem, DifferItem) -> DifferItem?) = { old, new ->
            if (new::class.isInstance(old)) new else null
        }

        override val stableId: Long = feeder.adsbxId.hashCode().toLong()
    }

    class FeederVH(parent: ViewGroup) : ModularAdapter.VH(R.layout.feeder_list_line, parent),
        BindableVH<Item, FeederListLineBinding> {

        override val viewBinding: Lazy<FeederListLineBinding> = lazy { FeederListLineBinding.bind(itemView) }

        override val onBindData: FeederListLineBinding.(
            item: Item,
            payloads: List<Any>
        ) -> Unit = binding { item ->
            val feeder = item.feeder
            label.text = feeder.label

            lastSeen.text = if (feeder.lastSeenAt == Instant.MIN) {
                getString(R.string.last_seen_not_seen_label)
            } else {
                val timeAgo = DateUtils.getRelativeTimeSpanString(
                    feeder.lastSeenAt.toEpochMilli(),
                    Instant.now().toEpochMilli(),
                    DateUtils.FORMAT_ABBREV_RELATIVE.toLong()
                )
                "Seen $timeAgo"
            }

            planesInfoLast.text = if (feeder.lastError != null) {
                feeder.lastError.toString()
            } else {
                "Tracking ${feeder.aircraft.size} aircrafts. Aircraft count logged ${feeder.stats.aircraftCountLogged} times."
            }

            itemView.setOnClickListener { item.onClickAction(feeder) }
        }
    }
}