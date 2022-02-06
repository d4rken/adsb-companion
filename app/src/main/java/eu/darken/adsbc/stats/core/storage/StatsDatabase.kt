package eu.darken.adsbc.stats.core.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dagger.hilt.android.qualifiers.ApplicationContext
import eu.darken.adsbc.common.room.CommonConverters
import javax.inject.Inject


@Database(
    entities = [FeederStatsData::class],
    version = 1
)
@TypeConverters(CommonConverters::class)
abstract class StatsDatabase : RoomDatabase() {
    abstract fun statsDao(): FeederStatsDao

    class Factory @Inject constructor(@ApplicationContext private val context: Context) {
        fun create(): StatsDatabase = Room
            .databaseBuilder(context, StatsDatabase::class.java, "stats.db")
            .build()
    }
}