package cz.petstore2025.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.petstore2025.R
import cz.petstore2025.communication.CommunicationResult
import cz.petstore2025.communication.IPetsRemoteRepository
import cz.petstore2025.ui.screens.list.ListOfPetsScreenError
import cz.petstore2025.ui.screens.list.ListOfPetsScreenUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val petsRemoteRepository: IPetsRemoteRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<PetDetailScreenUIState> = MutableStateFlow(value = PetDetailScreenUIState())
    val uiState: StateFlow<PetDetailScreenUIState> get() = _uiState

    fun loadPetDetail(petId: Long){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                petsRemoteRepository.findPetById(petId)
            }

            when (result) {
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = _uiState.value.copy(
                        error = R.string.no_internet_connection
                    )
                }

                is CommunicationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = R.string.failed_to_load_pets
                    )
                }

                is CommunicationResult.Exception -> {
                    _uiState.value = _uiState.value.copy(
                        error = R.string.exception
                    )
                }

                is CommunicationResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        pet = result.data
                    )
                }
            }


        }
    }


}