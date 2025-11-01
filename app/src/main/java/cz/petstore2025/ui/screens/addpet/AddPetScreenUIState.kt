package cz.petstore2025.ui.screens.addpet

import android.net.Uri

data class AddPetScreenUIState(
    val loading: Boolean = false,
    val name: String = "",
    val categorySelection: String = "",
    val tags: List<String> = emptyList(),
    val photoUris: List<Uri> = emptyList(),
    val status: String = "available",
    val error: Int? = null,
    val success: Boolean = false
)

