package eu.darken.adsbc.aircraft.core

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class AircraftId(val id: String) : Parcelable