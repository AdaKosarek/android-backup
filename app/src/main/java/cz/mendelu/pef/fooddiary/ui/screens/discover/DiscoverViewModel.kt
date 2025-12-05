package cz.mendelu.pef.fooddiary.ui.screens.discover

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.communication.CommunicationResult
import cz.mendelu.pef.fooddiary.communication.IFoodsRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val foodsRepository: IFoodsRemoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DiscoverScreenUIState())
    val uiState: StateFlow<DiscoverScreenUIState> = _uiState

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                foodsRepository.getAllRecipes()
            }

            when (result) {
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = DiscoverScreenError(R.string.no_internet_connection)
                    )
                }

                is CommunicationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = DiscoverScreenError(R.string.failed_to_load_recipes)
                    )
                }

                is CommunicationResult.Exception -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = DiscoverScreenError(R.string.exception)
                    )
                }

                is CommunicationResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        recipes = result.data.results
                    )
                }
            }
        }
    }
}
