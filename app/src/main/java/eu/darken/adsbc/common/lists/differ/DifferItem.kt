package eu.darken.adsbc.common.lists.differ

import eu.darken.bb.common.lists.ListItem

interface DifferItem : ListItem {
    val stableId: Long

    val payloadProvider: ((DifferItem, DifferItem) -> DifferItem?)?
        get() = null
}