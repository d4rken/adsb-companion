package eu.darken.adsbc.stats.core.api

import dagger.Reusable
import eu.darken.adsbc.common.coroutine.DispatcherProvider
import eu.darken.adsbc.common.debug.logging.log
import eu.darken.adsbc.common.debug.logging.logTag
import eu.darken.adsbc.feeder.core.ADSBxId
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Instant
import java.util.regex.Pattern
import javax.inject.Inject

@Reusable
class ADSBxStatsEndpoint @Inject constructor(
    private val baseClient: OkHttpClient,
    private val moshiConverterFactory: MoshiConverterFactory,
    private val dispatcherProvider: DispatcherProvider,
) {
    private val api by lazy<ADSBxStatsApi> {
        val configHttpClient = baseClient.newBuilder().apply {

        }.build()

        Retrofit.Builder()
            .client(configHttpClient)
            .baseUrl("https://www.adsbexchange.com/")
            .addConverterFactory(moshiConverterFactory)
            .build()
            .create(ADSBxStatsApi::class.java)
    }

    suspend fun getFeederStats(id: ADSBxId): ADSBxStatsApi.FeederInfo = withContext(dispatcherProvider.IO) {
        log(TAG) { "getFeederStats(id=$id)" }

        val body = api.getStats(id.value)

        val bodyRaw = body.string()
        val jsoup = Jsoup.parse(bodyRaw)

        ADSBxStatsApi.FeederInfo(
            lastSeenAt = parseLastSeen(jsoup),
            aircraftCountLogged = parseAircraftCountLogged(jsoup)
        )
    }

    private fun parseAircraftCountLogged(doc: Document): Int {
        val infoText = doc
            .select("div")
            .firstOrNull { it.text().contains("Aircraft count logged") }
            ?.text()
            ?: throw IllegalArgumentException("Failed to find div with aircraft count logged")

        val matcher = LOG_COUNT_PATTERN.matcher(infoText)

        if (!matcher.matches()) throw IllegalArgumentException("Aircraft count log pattern doesn't match")

        val logCount = matcher.group(1)!!.toInt()
        log(TAG) { "Parsed aircraft log count: $logCount" }
        return logCount
    }

    private fun parseLastSeen(doc: Document): Instant {
        val infoText = doc
            .select("div")
            .firstOrNull { it.text().contains("seconds since epoc") }
            ?.text()
            ?: throw IllegalArgumentException("Failed find div with last seen")

        val matcher = LAST_SEEN_PATTERN.matcher(infoText)

        if (!matcher.matches()) throw IllegalArgumentException("Last seen pattern doesn't match.")

        val lastSeen = Instant.ofEpochMilli((matcher.group(1)!!.toDouble() * 1000).toLong())
        log(TAG) { "Parsed last seen: $lastSeen" }
        return lastSeen
    }

    companion object {
        private val LOG_COUNT_PATTERN = Pattern.compile(
            "^Aircraft count logged (\\d+) times \\(every 60 seconds\\).+?\$"
        )
        private val LAST_SEEN_PATTERN = Pattern.compile(
            "^.+?last seen \\((\\d+\\.\\d+) seconds since epoc\\).+?\$"
        )
        private val TAG = logTag("Feeder", "Stats", "Endpoint")
    }
}