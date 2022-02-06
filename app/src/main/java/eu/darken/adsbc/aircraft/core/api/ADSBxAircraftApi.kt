package eu.darken.adsbc.aircraft.core.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ADSBxAircraftApi {

    @JsonClass(generateAdapter = true)
    data class Error(
        @Json(name = "error") val error: String, // {"error": "Data Not Found - Anywhere ID [124124asd]"}
    )

    @GET("/uuid/?")
    suspend fun getAircraft(@Query("feed") feedId: String): ResponseBody

    @JsonClass(generateAdapter = true)
    data class AircraftPayload(
        @Json(name = "now") val now: Double,
        @Json(name = "messages") val messages: Int,
        @Json(name = "aircraft") val aircraft: List<TrackingData>,
    ) {

        @JsonClass(generateAdapter = true)
        data class TrackingData(
            @Json(name = "hex") val hex: String, //"040170"
            @Json(name = "type") val type: String, //"adsb_icao"
            @Json(name = "flight") val flight: String?, //"ETH701 "
            @Json(name = "alt_baro") val alt_baro: String?, //37000|ground
            @Json(name = "alt_geom") val alt_geom: Long?, //37025
            @Json(name = "gs") val gs: Double?, //581.4
            @Json(name = "ias") val ias: Int?, //276
            @Json(name = "tas") val tas: Int?, //476
            @Json(name = "mach") val mach: Double?, //0.844
            @Json(name = "wd") val wd: Int?, //329
            @Json(name = "ws") val ws: Int?, //132
            @Json(name = "oat") val oat: Long?, //-64
            @Json(name = "tat") val tat: Long?, //-34
            @Json(name = "track") val track: Double?, //115.9
            @Json(name = "track_rate") val track_rate: Float?, //0.0
            @Json(name = "roll") val roll: Float?, //-0.18
            @Json(name = "mag_heading") val mag_heading: Float?, //104.59
            @Json(name = "true_heading") val true_heading: Float?, //107.22
            @Json(name = "baro_rate") val baro_rate: Int?, //0
            @Json(name = "geom_rate") val geom_rate: Int?, //0
            @Json(name = "squawk") val squawk: String?, //"2271"
            @Json(name = "emergency") val emergency: String?, //"none"
            @Json(name = "category") val category: String?, //"A5"
            @Json(name = "nav_qnh") val nav_qnh: Float?, //1013.6
            @Json(name = "nav_altitude_mcp") val nav_altitude_mcp: Int?, //36992
            @Json(name = "nav_heading") val nav_heading: Float?, //104.77
            @Json(name = "lat") val lat: Float?, //49.717464
            @Json(name = "lon") val lon: Float?, //6.801576
            @Json(name = "nic") val nic: Int?, //8
            @Json(name = "rc") val rc: Int?, //186
            @Json(name = "seen_pos") val seen_pos: Float?, //0.6
            @Json(name = "version") val version: Int?, //2
            @Json(name = "nic_baro") val nic_baro: Int?, //1
            @Json(name = "nac_p") val nac_p: Int?, //9
            @Json(name = "nac_v") val nac_v: Int?, //2
            @Json(name = "sil") val sil: Int?, //3
            @Json(name = "sil_type") val sil_type: String?, //"perhour"
            @Json(name = "gva") val gva: Int?, //2
            @Json(name = "sda") val sda: Int?, //2
            @Json(name = "alert") val alert: Int?, //0
            @Json(name = "spi") val spi: Int?, //0
            @Json(name = "mlat") val mlat: List<String>, // ["lat","lon","nic","rc","nac_p","nac_v","sil","sil_type"]
            @Json(name = "tisb") val tisb: List<String>,
            @Json(name = "messages") val messages: Int, //1536
            @Json(name = "seen") val seen: Float, //0.6
            @Json(name = "rssi") val rssi: Float, //-29.3
        )
    }
}