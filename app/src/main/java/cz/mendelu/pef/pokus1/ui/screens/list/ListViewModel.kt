package cz.mendelu.pef.pokus1.ui.screens.list
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.pokus1.R
import cz.mendelu.pef.pokus1.communication.CommunicationResult
import cz.mendelu.pef.pokus1.communication.api.IStopsRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    private val stopsRemoteRepository: IStopsRemoteRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ListScreenUIState> =
        MutableStateFlow(ListScreenUIState())

    val uiState: StateFlow<ListScreenUIState>
        get() = _uiState

    init {
        loadStops()
    }

    private fun loadStops() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                stopsRemoteRepository.getStops()
            }

            when (result) {
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = ListScreenError(R.string.no_internet_connection)
                    )
                }

                is CommunicationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = ListScreenError(R.string.failed_to_load_stops)
                    )
                }

                is CommunicationResult.Exception -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = ListScreenError(R.string.exception)
                    )
                }

                is CommunicationResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        stops = result.data.stops
                    )
                }
            }
        }
    }
}
