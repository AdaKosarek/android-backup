package cz.mendelu.pef.fooddiary.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.communication.CommunicationResult
import cz.mendelu.pef.fooddiary.communication.IFoodsRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodsRepository: IFoodsRemoteRepository
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
}
