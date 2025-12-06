package cz.mendelu.pef.fooddiary.ui.screens.detail

import cz.mendelu.pef.fooddiary.model.RecipeDetail

data class FoodDetailScreenUIState(
    val loading: Boolean = true,
    val recipe: RecipeDetail? = null,
    val error: Int? = null
)
