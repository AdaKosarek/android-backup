package cz.mendelu.pef.fooddiary.ui.screens.map

import cz.mendelu.pef.fooddiary.database.SavedMeal

interface MapScreenActions {
    fun onMealSelected(meal: SavedMeal?)
}