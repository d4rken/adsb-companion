package eu.darken.adsbc.feeder.core

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class ADSBxId(val value: String) : Parcelable {

    init {
        if (value.isEmpty()) throw IllegalArgumentException("ID can't be empty or blank.")
    }

    override fun toString(): String = "FeederId($value)"
}