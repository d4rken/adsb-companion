package eu.darken.adsbc.feeder.ui.list.actions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import eu.darken.adsbc.R
import eu.darken.adsbc.common.DialogActionEnum

enum class FeederAction constructor(
    @DrawableRes val iconRes: Int,
    @StringRes val labelRes: Int
) : DialogActionEnum {
    RENAME(R.drawable.ic_baseline_drive_file_rename_outline_24, R.string.feeder_rename_action),
    REFRESH(R.drawable.ic_baseline_refresh_24, R.string.feeder_refresh_action),
    DELETE(R.drawable.ic_baseline_delete_sweep_24, R.string.feeder_delete_action)
}