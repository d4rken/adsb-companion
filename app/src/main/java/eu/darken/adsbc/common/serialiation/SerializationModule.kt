package eu.darken.adsbc.common.serialiation

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SerializationModule {

    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder()
        .build()
}