package cz.petstore2025.ui.screens.list

import java.io.Serializable

data class ListOfPetsScreenUIState(
    var loading: Boolean = true,
    var error: ListOfPetsScreenError? = null)