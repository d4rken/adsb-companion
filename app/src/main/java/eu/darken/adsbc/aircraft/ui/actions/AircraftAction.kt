package eu.darken.adsbc.aircraft.ui.actions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import eu.darken.adsbc.R
import eu.darken.adsbc.common.DialogActionEnum

enum class AircraftAction constructor(
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int
) : DialogActionEnum {
    DETAILS(R.drawable.ic_baseline_drive_file_rename_outline_24, R.string.aircraft_details_action),
}