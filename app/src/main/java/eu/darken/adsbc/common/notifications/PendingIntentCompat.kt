package eu.darken.adsbc.common.notifications

import android.app.PendingIntent
import eu.darken.adsbc.common.hasApiLevel

object PendingIntentCompat {
    val FLAG_IMMUTABLE: Int = if (hasApiLevel(31)) {
        PendingIntent.FLAG_IMMUTABLE
    } else {
        0
    }
}