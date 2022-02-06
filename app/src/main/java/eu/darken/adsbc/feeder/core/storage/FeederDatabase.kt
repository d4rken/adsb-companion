package eu.darken.adsbc.feeder.core.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.adsbc.common.room.CommonConverters
import javax.inject.Inject


@Database(
    entities = [FeederData::class],
    version = 1
)
@TypeConverters(CommonConverters::class)
abstract class FeederDatabase : RoomDatabase() {

    abstract fun feederDao(): FeederDao

    class Factory @Inject constructor(@ApplicationContext private val context: Context) {
        fun create(): FeederDatabase = Room
            .databaseBuilder(context, FeederDatabase::class.java, "feeder.db")
            .build()
    }
}