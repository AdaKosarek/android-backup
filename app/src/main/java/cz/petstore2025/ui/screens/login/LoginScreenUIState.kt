package cz.petstore2025.ui.screens.login

data class LoginScreenUIState(
    val username: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
