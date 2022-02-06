package eu.darken.adsbc.feeder.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class FeederId(
    val value: String = UUID.randomUUID().toString()
) : Parcelable