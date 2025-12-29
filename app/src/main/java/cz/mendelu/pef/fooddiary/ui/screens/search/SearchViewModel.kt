package cz.mendelu.pef.fooddiary.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.communication.CommunicationResult
import cz.mendelu.pef.fooddiary.communication.IFoodsRemoteRepository
import cz.mendelu.pef.fooddiary.model.DiscoverRecipeItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val foodsRepository: IFoodsRemoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUIState())
    val uiState: StateFlow<SearchScreenUIState> = _uiState

    private var searchJob: Job? = null

    fun onQueryChange(text: String) {
        _uiState.value = _uiState.value.copy(
            query = text,
            error = null
        )

        searchJob?.cancel()

        if (text.length < 2) {
            _uiState.value = _uiState.value.copy(
                loading = false,
                recipes = null,
                selectedRecipe = null
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)

            _uiState.value = _uiState.value.copy(loading = true)

            val result = withContext(Dispatchers.IO) {
                foodsRepository.searchRecipes(text)
            }

            when (result) {
                is CommunicationResult.ConnectionError ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = SearchScreenError(R.string.no_internet_connection)
                    )

                is CommunicationResult.Error ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = SearchScreenError(R.string.failed_to_load_recipes)
                    )

                is CommunicationResult.Exception ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = SearchScreenError(R.string.exception)
                    )

                is CommunicationResult.Success ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        recipes = result.data.results
                    )
            }
        }
    }

    fun selectRecipe(recipe: DiscoverRecipeItem) {
        _uiState.value = _uiState.value.copy(
            selectedRecipe = recipe
        )
    }

    fun selectNone() {
        _uiState.value = _uiState.value.copy(
            selectedRecipe = null
        )
    }
}
