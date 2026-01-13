package cz.mendelu.pef.pokus1.ui.screens.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.mendelu.pef.pokus1.R
import cz.mendelu.pef.pokus1.communication.CommunicationResult
import cz.mendelu.pef.pokus1.communication.api.IStopsRemoteRepository
import cz.mendelu.pef.pokus1.map.StopClusterItem
import cz.mendelu.pef.pokus1.model.Stop
import cz.mendelu.pef.pokus1.ui.screens.list.ListScreenError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MapViewModel @Inject constructor(
    private val stopsRemoteRepository: IStopsRemoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapScreenUIState())
    val uiState: StateFlow<MapScreenUIState> = _uiState

    init {
        loadStops()
    }

    private fun loadStops() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)

            when (val result = stopsRemoteRepository.getStops()) {

                is CommunicationResult.Success -> {
                    val items = result.data.stops
                        .filter { it.latitude != null && it.longitude != null }
                        .map { StopClusterItem(it) }

                    _uiState.value = MapScreenUIState(
                        loading = false,
                        stops = items,
                        initialCameraPosition =
                            items.firstOrNull()?.position
                    )
                }


                is CommunicationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = MapScreenError(R.string.failed_to_load_stops)
                    )
                }

                is CommunicationResult.Exception -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = MapScreenError(R.string.exception)
                    )
                }

                is CommunicationResult.ConnectionError -> {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        error = MapScreenError(R.string.no_internet_connection)
                    )
                }
            }
        }
    }

    fun onStopSelected(stop: Stop?) {
        _uiState.value = _uiState.value.copy(
            selectedStop = stop
        )
    }
}
