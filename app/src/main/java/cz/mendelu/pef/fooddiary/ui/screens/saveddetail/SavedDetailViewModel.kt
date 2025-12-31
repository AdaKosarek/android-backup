package cz.mendelu.pef.fooddiary.ui.screens.saveddetail
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.utils.ImageStorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedDetailViewModel @Inject constructor(
    private val savedMealsRepository: ISavedMealsLocalRepository,
    private val imageStorageRepository: ImageStorageRepository
) : ViewModel(), SavedDetailActions{

    private val _uiState = MutableStateFlow(SavedDetailScreenUIState())
    val uiState: StateFlow<SavedDetailScreenUIState> = _uiState.asStateFlow()

    fun loadMeal(localId: Long) {
        _uiState.value = SavedDetailScreenUIState(
            loading = true,
            meal = null,
            error = null
        )

        viewModelScope.launch {
            try {
                val meal = savedMealsRepository.getById(localId)
                _uiState.value = SavedDetailScreenUIState(
                    loading = false,
                    meal = meal,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = SavedDetailScreenUIState(
                    loading = false,
                    meal = null,
                    error = R.string.failed_to_load_recipes
                )
            }
        }
    }

    override fun onDeleteMeal() {
        val meal = _uiState.value.meal ?: return

        viewModelScope.launch {
            meal.userPhotoUri?.let {
                imageStorageRepository.deletePhoto(it)
            }

            savedMealsRepository.delete(meal)

            _uiState.value = _uiState.value.copy(
                deletedSuccessfully = true
            )
        }
    }

    override fun onToggleFavorite() {
        val meal = _uiState.value.meal ?: return

        val updatedMeal = meal.copy(
            isFavorite = !meal.isFavorite
        )

        viewModelScope.launch {
            savedMealsRepository.update(updatedMeal)

            _uiState.value = _uiState.value.copy(
                meal = updatedMeal
            )
        }
    }

    //edit
    override fun onEditNameChange(value: String) {
        _uiState.value = _uiState.value.copy(editName = value)
    }

    override fun onEditNoteChange(value: String) {
        _uiState.value = _uiState.value.copy(editNote = value)
    }

    override fun onEditPlaceNameChange(value: String) {
        _uiState.value = _uiState.value.copy(editPlaceName = value)
    }

    override fun onToggleEdit() {
        val state = _uiState.value
        val meal = state.meal ?: return

        if (!state.isEditing) {
            _uiState.value = state.copy(
                isEditing = true,
                editName = meal.customName.orEmpty(),
                editNote = meal.userNote.orEmpty(),
                editPlaceName = meal.placeName.orEmpty()
            )
        } else {
            if (state.editName.isBlank()) return

            val updated = meal.copy(
                customName = state.editName,
                userNote = state.editNote,
                placeName = state.editPlaceName.ifBlank { null }
            )

            viewModelScope.launch {
                savedMealsRepository.update(updated)
                _uiState.value = state.copy(
                    isEditing = false,
                    meal = updated
                )
            }
        }
    }

}
