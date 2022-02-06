package eu.darken.adsbc.stats.core.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.Instant

interface ADSBxStatsApi {

    @GET("/api/feeders/?")
    suspend fun getStats(@Query("feed") feedId: String): ResponseBody

    data class FeederInfo(
        val lastSeenAt: Instant,
        val aircraftCountLogged: Int,
    )

}