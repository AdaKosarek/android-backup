package cz.petstore2025.ui.activities

sealed class SplashScreenUiState {
    object Default : SplashScreenUiState()
    object ShowLogin : SplashScreenUiState()
    object ContinueToApp : SplashScreenUiState()
}