package eu.darken.adsbc.feeder.core

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.util.*

@Keep
@Parcelize
data class FeederId(
    val value: String = UUID.randomUUID().toString()
) : Parcelable