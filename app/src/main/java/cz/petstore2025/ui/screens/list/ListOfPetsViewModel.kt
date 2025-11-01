package cz.petstore2025.ui.screens.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.petstore2025.R
import cz.petstore2025.communication.CommunicationResult
import cz.petstore2025.communication.IPetsRemoteRepository
import cz.petstore2025.model.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListOfPetsViewModel @Inject constructor(
    private val petsRemoteRepository: IPetsRemoteRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListOfPetsScreenUIState> =
        MutableStateFlow(value = ListOfPetsScreenUIState())
    val uiState: StateFlow<ListOfPetsScreenUIState> get() = _uiState

    init {
        loadPets()
    }

    private fun loadPets() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                petsRemoteRepository.findByStatus("available")
            }

            handleResult(result)
        }
    }

    fun reloadPets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)

            val result = withContext(Dispatchers.IO) {
                petsRemoteRepository.findByStatus("available")
            }

            handleResult(result)
        }
    }

    private fun handleResult(result: CommunicationResult<List<Pet>>) {
        when (result) {
            is CommunicationResult.ConnectionError -> {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = ListOfPetsScreenError(R.string.no_internet_connection)
                )
            }
            is CommunicationResult.Error -> {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = ListOfPetsScreenError(R.string.failed_to_load_pets)
                )
            }
            is CommunicationResult.Exception -> {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = ListOfPetsScreenError(R.string.exception)
                )
            }
            is CommunicationResult.Success -> {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    pets = result.data
                )
            }
        }
    }

}
