package cz.mendelu.pef.fooddiary.ui.screens.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedMealsRepository: ISavedMealsLocalRepository
) : ViewModel(), SettingsActions {

    private val _uiState = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState

    override fun onClearAppDataClick() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = true)
    }

    override fun onDismissClearDialog() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = false)
    }

    override fun onConfirmClearAppData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                showConfirmDialog = false,
                isClearingData = true
            )

            try {
                savedMealsRepository.deleteAll()

                _uiState.value = _uiState.value.copy(
                    isClearingData = false,
                    dataCleared = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isClearingData = false,
                    error = R.string.exception
                )
            }
        }
    }
}


