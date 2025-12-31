package cz.mendelu.pef.fooddiary.ui.screens.saveddetail

import cz.mendelu.pef.fooddiary.database.SavedMeal

data class SavedDetailScreenUIState(
    val loading: Boolean = true,
    val meal: SavedMeal? = null,

    val isEditing: Boolean = false,
    val editName: String = "",
    val editNote: String = "",
    val editPlaceName: String = "",

    val error: Int? = null,
    val deletedSuccessfully: Boolean = false
)

