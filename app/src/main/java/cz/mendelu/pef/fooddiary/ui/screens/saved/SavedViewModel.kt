package cz.mendelu.pef.fooddiary.ui.screens.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val repository: ISavedMealsLocalRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<SavedScreenUIState>(SavedScreenUIState.Default)
    val uiState: StateFlow<SavedScreenUIState> = _uiState.asStateFlow()

    fun loadMeals() {
        viewModelScope.launch {
            repository.getAll().collect { meals ->
                _uiState.value = SavedScreenUIState.Success(meals)
            }
        }
    }
}