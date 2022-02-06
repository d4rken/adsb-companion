package eu.darken.adsbc.common

import eu.darken.adsbc.common.lists.differ.DifferItem

interface DialogActionEnum : DifferItem {
    override val stableId: Long
        get() = (this as Enum<*>).ordinal.toLong()
}