package cz.petstore2025.ui.screens.list

import cz.petstore2025.model.Pet
import java.io.Serializable

data class ListOfPetsScreenUIState(
    var loading: Boolean = true,
    val pets: List<Pet>? = null,
    var error: ListOfPetsScreenError? = null)