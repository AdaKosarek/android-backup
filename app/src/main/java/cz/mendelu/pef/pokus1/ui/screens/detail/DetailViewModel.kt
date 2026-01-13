package cz.mendelu.pef.pokus1.ui.screens.detail
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.pef.pokus1.R
import cz.mendelu.pef.pokus1.communication.CommunicationResult
import cz.mendelu.pef.pokus1.communication.api.IStopsRemoteRepository
import cz.mendelu.pef.pokus1.communication.api1.IPackagesRemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val stopsRemoteRepository: IStopsRemoteRepository,
    private val packagesRemoteRepository: IPackagesRemoteRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(DetailScreenUIState())

    val uiState: StateFlow<DetailScreenUIState>
        get() = _uiState

    fun loadStopDetail(stopId: Long) {
        viewModelScope.launch {

            val stopsResult = withContext(Dispatchers.IO) {
                stopsRemoteRepository.getStops()
            }

            val packagesResult = withContext(Dispatchers.IO) {
                packagesRemoteRepository.getPackages()
            }


            if (stopsResult !is CommunicationResult.Success ||
                packagesResult !is CommunicationResult.Success
            ) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = DetailScreenError(R.string.failed_to_load_detail)
                )
                return@launch
            }

            val stop = stopsResult.data.stops.firstOrNull {
                it.id == stopId
            }

            val packagesForStop = packagesResult.data.filter {
                it.stopId != null && it.stopId == stopId
            }

            val totalWeight = packagesForStop.sumOf {
                it.weightKg ?: 0.0
            }

            _uiState.value = _uiState.value.copy(
                loading = false,
                stop = stop,
                packageCount = packagesForStop.size,
                totalWeight = totalWeight,
                packages = packagesForStop,
            )
        }
    }
}
