package cz.mendelu.pef.fooddiary.ui.screens.search

import java.io.Serializable


data class SearchScreenError(
    val communicationError: Int
) : Serializable