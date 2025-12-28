package cz.mendelu.pef.fooddiary.ui.screens.saved

import cz.mendelu.pef.fooddiary.database.SavedMeal

sealed class SavedScreenUIState {
    object Default : SavedScreenUIState()
    data class Success(val meals: List<SavedMeal>) : SavedScreenUIState()
}