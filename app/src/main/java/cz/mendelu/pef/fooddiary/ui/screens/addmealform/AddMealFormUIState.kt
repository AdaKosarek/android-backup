package cz.mendelu.pef.fooddiary.ui.screens.addmealform

import cz.mendelu.pef.fooddiary.model.RecipeDetail

data class AddMealFormUIState(
    val loading: Boolean = false,

    //API
    val recipe: RecipeDetail? = null,

    val customName: String = "",
    val userNote: String = "",
    val userPhotoUri: String? = null,
    val placeName: String = "",

    val error: AddMealFormError? = null,
    val savedSuccessfully: Boolean = false
)

