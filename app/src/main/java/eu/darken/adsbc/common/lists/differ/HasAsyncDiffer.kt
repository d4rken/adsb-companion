package eu.darken.bb.common.lists.differ

import eu.darken.adsbc.common.lists.differ.DifferItem

interface HasAsyncDiffer<T : DifferItem> {

    val data: List<T>
        get() = asyncDiffer.currentList

    val asyncDiffer: AsyncDiffer<*, T>

}