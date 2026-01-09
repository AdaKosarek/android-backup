package cz.mendelu.pef.fooddiary.ui.screens.discover

import androidx.annotation.StringRes
import cz.mendelu.pef.fooddiary.R

enum class FoodCategory(
    val apiValue: String?,
    @StringRes val labelRes: Int
) {
    ALL(null, R.string.all),
    MAIN_DISH("main dish", R.string.main_dish),
    SOUP("soup", R.string.soup),
    DESSERT("dessert", R.string.desert),
    SIDE_DISH("side dish", R.string.side_dish)
}
