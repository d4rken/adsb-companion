package eu.darken.adsbc.aircraft.core

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class AircraftId(val id: String) : Parcelable