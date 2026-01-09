package cz.mendelu.pef.fooddiary.ui.screens.settings

import cz.mendelu.pef.fooddiary.BuildConfig

data class SettingsUIState(
    val isClearingData: Boolean = false,
    val dataCleared: Boolean = false,
    val error: Int? = null,
    val appVersion: String = BuildConfig.VERSION_NAME,
    val showConfirmDialog: Boolean = false,
)