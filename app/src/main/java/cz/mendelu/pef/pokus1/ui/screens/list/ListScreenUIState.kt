package cz.mendelu.pef.pokus1.ui.screens.list

import cz.mendelu.pef.pokus1.model.Stop

data class ListScreenUIState(
    val loading: Boolean = true,
    val stops: List<Stop>? = null,
    val error: ListScreenError? = null
)