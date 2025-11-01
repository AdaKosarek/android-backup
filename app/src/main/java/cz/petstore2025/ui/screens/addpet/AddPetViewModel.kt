package cz.petstore2025.ui.screens.addpet

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.petstore2025.R
import cz.petstore2025.communication.CommunicationResult
import cz.petstore2025.communication.IPetsRemoteRepository
import cz.petstore2025.model.Category
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
    private val petsRemoteRepository: IPetsRemoteRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPetScreenUIState())
    val uiState: StateFlow<AddPetScreenUIState> get() = _uiState

    fun onNameChanged(newName: String) {
        _uiState.value = _uiState.value.copy(name = newName)
    }

    fun onCategorySelectionChanged(newCategory: String) {
        _uiState.value = _uiState.value.copy(categorySelection = newCategory)
    }

    fun onTagsChanged(newTags: List<String>) {
        _uiState.value = _uiState.value.copy(tags = newTags)
    }

    fun onPhotoUrisChange(newUris: List<Uri>) {
        _uiState.value = _uiState.value.copy(photoUris = newUris)
    }

    fun showTemporaryLoading() {
        _uiState.value = _uiState.value.copy(loading = true)
    }

    fun addPet() {
        val current = _uiState.value

        if (current.name.isBlank()) {
            _uiState.value = current.copy(error = R.string.name_required)
            return
        }
        if (current.photoUris.isEmpty()) {
            _uiState.value = current.copy(error = R.string.photo_required)
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(loading = true, error = null)

            val categoryId = when (current.categorySelection) {
                "Dogs" -> 1L
                "Cats" -> 2L
                "Birds" -> 3L
                "Fishes" -> 4L
                "Other" -> 5L
                else -> 6L
            }

            val category = if (current.categorySelection.isNotBlank()) {
                Category(id = categoryId, name = current.categorySelection.trim())
            } else null

            val tagsList = if (current.tags.isNotEmpty()) {
                current.tags.mapIndexed { index, tagName ->
                    Tag(id = index.toLong() + 1, name = tagName.trim())
                }
            } else null

            val photoUrls = current.photoUris.map { it.toString() }

            val newPet = Pet(
                id = System.currentTimeMillis(),
                category = category,
                name = current.name.trim(),
                photoUrls = photoUrls,
                tags = tagsList,
                status = "available"
            )

            when (val petResult = withContext(Dispatchers.IO) {
                petsRemoteRepository.addPet(newPet)
            }) {
                is CommunicationResult.Success -> {
                    _uiState.value = current.copy(loading = false, success = true)
                }
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = current.copy(loading = false, error = R.string.no_internet_connection)
                }
                is CommunicationResult.Error -> {
                    _uiState.value = current.copy(loading = false, error = R.string.failed_to_add_pet)
                }
                is CommunicationResult.Exception -> {
                    _uiState.value = current.copy(loading = false, error = R.string.exception)
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}




