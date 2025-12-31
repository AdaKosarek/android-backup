package cz.mendelu.pef.fooddiary.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.communication.CommunicationResult
import cz.mendelu.pef.fooddiary.communication.IFoodsRemoteRepository
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.model.SavedMealSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodsRepository: IFoodsRemoteRepository,
    private val savedMealsRepository: ISavedMealsLocalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodDetailScreenUIState())
    val uiState: StateFlow<FoodDetailScreenUIState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: Long) {
        _uiState.value = FoodDetailScreenUIState(
            loading = true,
            recipe = null,
            error = null
        )

        viewModelScope.launch {
            when (val result = foodsRepository.getRecipeById(recipeId)) {

                is CommunicationResult.Success -> {
                    _uiState.value = FoodDetailScreenUIState(
                        loading = false,
                        recipe = result.data,
                        error = null
                    )
                }

                is CommunicationResult.Error -> {
                    _uiState.value = FoodDetailScreenUIState(
                        loading = false,
                        recipe = null,
                        error = R.string.failed_to_load_recipes
                    )
                }

                is CommunicationResult.ConnectionError -> {
                    _uiState.value = FoodDetailScreenUIState(
                        loading = false,
                        recipe = null,
                        error = R.string.no_internet_connection
                    )
                }

                is CommunicationResult.Exception -> {
                    _uiState.value = FoodDetailScreenUIState(
                        loading = false,
                        recipe = null,
                        error = R.string.exception
                    )
                }
            }
        }
    }

    fun saveRecipe() {
        val recipe = _uiState.value.recipe ?: return

        val savedMeal = SavedMeal(
            source = SavedMealSource.API_ONLY,

            apiId = recipe.id,
            title = recipe.title,
            apiImage = recipe.image,
            readyInMinutes = recipe.readyInMinutes,
            servings = recipe.servings,
            dishTypes = recipe.dishTypes,

            nutrition = recipe.nutrition,
            extendedIngredients = recipe.extendedIngredients,
            instructions = recipe.instructions,
            analyzedInstructions = recipe.analyzedInstructions,

            // všechno ostatní NULL
            customName = null,
            userPhotoUri = null,
            userNote = null,
            latitude = null,
            longitude = null,
            placeName = null,

            isFavorite = false,
            savedTimestamp = System.currentTimeMillis()
        )

        viewModelScope.launch {
            savedMealsRepository.insert(savedMeal)
            _uiState.value = _uiState.value.copy(savedSuccessfully = true)
        }
    }

}
