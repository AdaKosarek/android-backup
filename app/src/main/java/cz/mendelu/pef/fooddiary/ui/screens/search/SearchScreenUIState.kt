package cz.mendelu.pef.fooddiary.ui.screens.search

import cz.mendelu.pef.fooddiary.model.DiscoverRecipeItem

data class SearchScreenUIState(
    val loading: Boolean = false,
    val query: String = "",
    val recipes: List<DiscoverRecipeItem>? = null,
    val selectedRecipe: DiscoverRecipeItem? = null,
    val error: SearchScreenError? = null
)
