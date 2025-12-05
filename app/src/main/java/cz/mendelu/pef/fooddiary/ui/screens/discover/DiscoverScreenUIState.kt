package cz.mendelu.pef.fooddiary.ui.screens.discover

import cz.mendelu.pef.fooddiary.model.DiscoverRecipeItem

data class DiscoverScreenUIState(
    val loading: Boolean = true,
    val recipes: List<DiscoverRecipeItem>? = null,
    val error: DiscoverScreenError? = null
)
