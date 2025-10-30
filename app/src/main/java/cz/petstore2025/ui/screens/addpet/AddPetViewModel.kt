package cz.petstore2025.ui.screens.addpet

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.petstore2025.R
import cz.petstore2025.communication.CommunicationResult
import cz.petstore2025.communication.IPetsRemoteRepository
import cz.petstore2025.model.Pet
import cz.petstore2025.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val petsRemoteRepository: IPetsRemoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPetScreenUIState())
    val uiState: StateFlow<AddPetScreenUIState> get() = _uiState

    fun onNameChanged(newName: String) {
        _uiState.value = _uiState.value.copy(name = newName)
    }

    fun onTagsChanged(newTags: List<String>) {
        _uiState.value = _uiState.value.copy(tags = newTags)
    }

    fun onPhotoUrisChanged(newUris: List<Uri>) {
        _uiState.value = _uiState.value.copy(photoUris = newUris)
    }


    fun onStatusChanged(newStatus: String) {
        _uiState.value = _uiState.value.copy(status = newStatus)
    }

    fun addPet() {
        val current = _uiState.value
        if (current.name.isBlank()) {
            _uiState.value = current.copy(error = R.string.name_required)
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(loading = true, error = null)
            val newPet = Pet(
                name = current.name,
                photoUrls = current.photoUris.map { it.toString() },
                tags = current.tags.mapIndexed { index, tagName ->
                    Tag(id = index.toLong(), name = tagName)
                },
                status = current.status
            )


            val result = withContext(Dispatchers.IO) {
                petsRemoteRepository.addPet(newPet)
            }

            when (result) {
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = current.copy(loading = false, error = R.string.no_internet_connection)
                }
                is CommunicationResult.Error -> {
                    _uiState.value = current.copy(loading = false, error = R.string.failed_to_add_pet)
                }
                is CommunicationResult.Exception -> {
                    _uiState.value = current.copy(loading = false, error = R.string.exception)
                }
                is CommunicationResult.Success -> {
                    _uiState.value = current.copy(loading = false, success = true)
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
