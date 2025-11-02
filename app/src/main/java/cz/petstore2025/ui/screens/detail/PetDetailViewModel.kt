package cz.petstore2025.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
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
    private val petsRemoteRepository: IPetsRemoteRepository,
    private val savedStateHandle: SavedStateHandle
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
                        error = R.string.failed_to_load_pet
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


    //vymazani
    fun deletePet(petId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, deletionSuccess = false, deletionError = null)
            val result = withContext(Dispatchers.IO) {
                petsRemoteRepository.deletePet(petId)
            }
            when (result) {
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        deletionError = R.string.no_internet_connection
                    )
                }
                is CommunicationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        deletionError = R.string.failed_to_delete_pet
                    )
                }
                is CommunicationResult.Exception -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        deletionError = R.string.exception
                    )
                }
                is CommunicationResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        deletionSuccess = true
                    )

                    savedStateHandle["refreshList"] = true
                }
            }
        }
    }

    //order
    fun orderPet(petId: Long, quantity: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, orderSuccess = false, orderError = null)

            val result = withContext(Dispatchers.IO) {
                petsRemoteRepository.orderPet(petId, quantity)
            }

            when (result) {
                is CommunicationResult.Success -> {
                    _uiState.value = _uiState.value.copy(loading = false, orderSuccess = true)
                    savedStateHandle["refreshList"] = true
                }
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = _uiState.value.copy(loading = false, orderError = R.string.no_internet_connection)
                }
                is CommunicationResult.Error -> {
                    _uiState.value = _uiState.value.copy(loading = false, orderError = R.string.failed_to_order_pet)
                }
                is CommunicationResult.Exception -> {
                    _uiState.value = _uiState.value.copy(loading = false, orderError = R.string.exception)
                }
            }
        }
    }


    fun clearOrderError() {
        _uiState.value = _uiState.value.copy(orderError = null)
    }
    fun clearDeletionError() {
        _uiState.value = _uiState.value.copy(deletionError = null)
    }

}