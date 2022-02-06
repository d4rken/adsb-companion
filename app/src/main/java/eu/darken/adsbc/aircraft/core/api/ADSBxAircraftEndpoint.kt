package eu.darken.adsbc.aircraft.core.api

import com.squareup.moshi.Moshi
import dagger.Reusable
import eu.darken.adsbc.aircraft.core.Aircraft
import eu.darken.adsbc.aircraft.core.BaseAircraft
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.feeder.core.ADSBxId
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Instant
import javax.inject.Inject
import kotlin.math.roundToLong

@Reusable
class ADSBxAircraftEndpoint @Inject constructor(
    private val baseClient: OkHttpClient,
    private val moshiConverterFactory: MoshiConverterFactory,
    private val moshi: Moshi,
    private val dispatcherProvider: DispatcherProvider,
) {

    private val aircraftAdapter = moshi.adapter(ADSBxAircraftApi.AircraftPayload::class.java)
    private val errorAdapter = moshi.adapter(ADSBxAircraftApi.Error::class.java)

    private val api by lazy<ADSBxAircraftApi> {
        val configHttpClient = baseClient.newBuilder().apply {

        }.build()

        Retrofit.Builder()
            .client(configHttpClient)
            .baseUrl("https://globe.adsbexchange.com")
            .addConverterFactory(moshiConverterFactory)
            .build()
            .create(ADSBxAircraftApi::class.java)
    }


    suspend fun getAircraft(id: ADSBxId): Collection<Aircraft> = withContext(dispatcherProvider.IO) {
        log(TAG) { "getAircrafts(id=$id)" }

        val body = api.getAircraft(id.value)
        val bodyRaw = body.string()

        val container = if (bodyRaw.contains("\"error\"")) {
            val error = errorAdapter.fromJson(bodyRaw)
            throw IllegalArgumentException(error?.error)
        } else {
            aircraftAdapter.fromJson(bodyRaw)!!
        }
        val containerNow = Instant.ofEpochMilli((container.now * 1000L).roundToLong())
        container.aircraft.map {
            val nowSeenAt = Instant.ofEpochMilli(((container.now - it.seen) * 1000L).roundToLong())

            BaseAircraft(
                hexCode = it.hex,
                callsign = it.flight,
                lastSeenAt = nowSeenAt,
                totalMessages = it.messages,
                messageRate = 0f,
                squawkCode = it.squawk,
                altitudeMeter = it.alt_geom,
                distanceKmh = 0f,
                reception = it.rssi
            )
        }
    }

    companion object {
        private val TAG = logTag("Feeder", "Aircraft", "Endpoint")
    }
}