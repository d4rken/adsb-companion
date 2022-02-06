package eu.darken.adsbc.common.http

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.darken.adsbc.common.debug.autoreport.DebugSettings
import eu.darken.adsbc.common.debug.logging.Logging.Priority.VERBOSE
import eu.darken.adsbc.common.debug.logging.log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class HttpModule {

    @Reusable
    @Provides
    fun defaultHttpClient(
        moshiConverterFactory: MoshiConverterFactory,
        debugSettings: DebugSettings,
    ): OkHttpClient {
        val interceptors: List<Interceptor> = listOf(
            HttpLoggingInterceptor { message ->
                log("HTTP", VERBOSE) { message }
            }.apply {
                if (debugSettings.isDebugModeEnabled.value) {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    setLevel(HttpLoggingInterceptor.Level.BASIC)
                }
            },
        )

        return OkHttpClient.Builder().apply {
            interceptors.forEach { addInterceptor(it) }
        }.build()
    }

    @Reusable
    @Provides
    fun moshiConverter(moshi: Moshi): MoshiConverterFactory = MoshiConverterFactory.create(moshi)

}
