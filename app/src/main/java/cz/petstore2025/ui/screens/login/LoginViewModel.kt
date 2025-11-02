package cz.petstore2025.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.petstore2025.communication.CommunicationResult
import cz.petstore2025.communication.IPetsRemoteRepository
import cz.petstore2025.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val petsRemoteRepository: IPetsRemoteRepository,
    private val dataStoreRepository: IDataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUIState())
    val uiState: StateFlow<LoginScreenUIState> = _uiState

    fun onUsernameChanged(newUsername: String) {
        _uiState.value = _uiState.value.copy(username = newUsername)
    }

    fun onPasswordChanged(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun login() {
        val current = _uiState.value
        if (current.username.isBlank() || current.password.isBlank()) {
            _uiState.value = current.copy(error = "Fill username and password")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(loading = true, error = null)

            val result = petsRemoteRepository.loginUser(current.username, current.password)

            when (result) {
                is CommunicationResult.Success -> {
                    if (result.data.code == 200) {
                        dataStoreRepository.setLoginSuccessful()
                        _uiState.value = current.copy(loading = false, success = true)
                    } else {
                        _uiState.value = current.copy(
                            loading = false,
                            error = "Incorrect login details"
                        )
                    }
                }
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = current.copy(
                        loading = false,
                        error = "Unable to connect to server"
                    )
                }
                is CommunicationResult.Error -> {
                    _uiState.value = current.copy(
                        loading = false,
                        error = "Incorrect login details"
                    )
                }
                is CommunicationResult.Exception -> {
                    _uiState.value = current.copy(
                        loading = false,
                        error = "An error occurred while logging in"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
