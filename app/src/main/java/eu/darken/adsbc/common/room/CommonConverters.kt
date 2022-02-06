package eu.darken.adsbc.common.room

import androidx.room.TypeConverter
import eu.darken.adsbc.feeder.core.ADSBxId
import eu.darken.adsbc.feeder.core.FeederId
import java.time.Instant
import java.util.*

class CommonConverters {

    @TypeConverter
    fun toUUID(value: String?): UUID? = value?.let { UUID.fromString(it) }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun toInstant(value: String?): Instant? = value?.let { Instant.parse(it) }

    @TypeConverter
    fun fromInstant(date: Instant?): String? = date?.toString()

    @TypeConverter
    fun toAdsbxId(value: String?): ADSBxId? = value?.let { ADSBxId(it) }

    @TypeConverter
    fun fromAdsbxId(date: ADSBxId?): String? = date?.value

    @TypeConverter
    fun toFeederId(value: String?): FeederId? = value?.let { FeederId(it) }

    @TypeConverter
    fun fromFeederId(id: FeederId?): String? = id?.value

}
