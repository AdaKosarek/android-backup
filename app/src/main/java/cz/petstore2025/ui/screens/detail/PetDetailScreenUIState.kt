package cz.petstore2025.ui.screens.detail

import cz.petstore2025.model.Pet
import cz.petstore2025.ui.screens.list.ListOfPetsScreenError
import java.io.Serializable

data class PetDetailScreenUIState(
    var loading: Boolean = true,
    val pet: Pet? = null,
    var error: Int? = null)