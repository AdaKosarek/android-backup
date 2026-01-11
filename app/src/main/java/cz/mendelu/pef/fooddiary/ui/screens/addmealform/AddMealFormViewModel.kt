package cz.mendelu.pef.fooddiary.ui.screens.addmealform

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.communication.CommunicationResult
import cz.mendelu.pef.fooddiary.communication.IFoodsRemoteRepository
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.model.SavedMealSource
import cz.mendelu.pef.fooddiary.utils.IImageStorageRepository
import cz.mendelu.pef.fooddiary.utils.ImageStorageRepository
import cz.mendelu.pef.fooddiary.utils.MealInputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMealFormViewModel @Inject constructor(
    private val foodsRepository: IFoodsRemoteRepository,
    private val savedMealsRepository: ISavedMealsLocalRepository,
    private val imageStorageRepository: IImageStorageRepository
) : ViewModel() {

    private var initialized = false
    private val _uiState = MutableStateFlow(AddMealFormUIState())
    val uiState: StateFlow<AddMealFormUIState> = _uiState
    fun init(apiId: Long?) {
        if (initialized) return
        initialized = true

        if (apiId != null) {
            loadRecipe(apiId)
        }
    }
    private fun loadRecipe(apiId: Long) {
        _uiState.value = _uiState.value.copy(
            loading = true,
            error = null
        )

        viewModelScope.launch {
            when (val result = foodsRepository.getRecipeById(apiId)) {

                is CommunicationResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        recipe = result.data
                    )
                }

                is CommunicationResult.ConnectionError -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = AddMealFormError(R.string.no_internet_connection)
                    )
                }

                else -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = AddMealFormError(R.string.failed_to_load_recipes)
                    )
                }
            }
        }
    }


    fun onCustomNameChange(value: String) {
        _uiState.value = _uiState.value.copy(customName = value)
    }

    fun onNoteChange(value: String) {
        _uiState.value = _uiState.value.copy(userNote = value)
    }

    fun onPlaceNameChange(value: String) {
        _uiState.value = _uiState.value.copy(placeName = value)
    }

    fun onPhotoSelected(uri: String) {
        _uiState.value = _uiState.value.copy(userPhotoUri = uri)
    }

    fun onUseLocationChange(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(useLocation = enabled)
    }

    fun saveMeal(
        latitude: Double?,
        longitude: Double?
    ) {
        val state = _uiState.value

        if (!MealInputValidator.isCustomNameValid(state.customName)) {
            _uiState.value = state.copy(
                error = AddMealFormError(R.string.custom_name_required)
            )
            return
        }

        val recipe = state.recipe

        val copiedPhotoUri = state.userPhotoUri?.let {
            imageStorageRepository.saveMealPhoto(Uri.parse(it))
        }

        val finalLat = latitude ?: 50.087451
        val finalLng = longitude ?: 14.420671

        val savedMeal = SavedMeal(
            source = if (recipe != null) SavedMealSource.API_PLUS_FAB  else SavedMealSource.FAB,
            apiId = recipe?.id,
            title = recipe?.title,
            apiImage = recipe?.image,
            readyInMinutes = recipe?.readyInMinutes,
            servings = recipe?.servings,
            dishTypes = recipe?.dishTypes,
            nutrition = recipe?.nutrition,
            extendedIngredients = recipe?.extendedIngredients,
            instructions = recipe?.instructions,
            analyzedInstructions = recipe?.analyzedInstructions,

            customName = state.customName,
            userPhotoUri = copiedPhotoUri,
            userNote = state.userNote,
            placeName = MealInputValidator.normalizePlaceName(state.placeName),

            latitude = finalLat,
            longitude = finalLng,

            savedTimestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            savedMealsRepository.insert(savedMeal)
            _uiState.value = state.copy(savedSuccessfully = true)
        }
    }
}

