package cz.mendelu.pef.fooddiary.ui.screens.map
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.map.SavedMealClusterItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val savedMealsRepository: ISavedMealsLocalRepository
) : ViewModel(), MapScreenActions {

    private val _uiState = MutableStateFlow(MapScreenUIState())
    val uiState: StateFlow<MapScreenUIState> = _uiState

    init {
        observeMeals()
    }

    private fun observeMeals() {
        viewModelScope.launch {
            savedMealsRepository.getAllForMap()
                .collect { meals ->

                    val items = meals
                        .groupBy { it.latitude to it.longitude }
                        .flatMap { (_, sameLocationMeals) ->
                            sameLocationMeals.mapIndexed { index, meal ->
                                val offset = index * 0.00003
                                SavedMealClusterItem(
                                    meal.copy(
                                        latitude = meal.latitude!! + offset,
                                        longitude = meal.longitude!! + offset
                                    )
                                )
                            }
                        }

                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        meals = items,
                        initialCameraPosition =
                            meals.firstOrNull()?.let {
                                LatLng(it.latitude!!, it.longitude!!)
                            }
                    )
                }
        }
    }

    override fun onMealSelected(meal: SavedMeal?) {
        _uiState.value = _uiState.value.copy(
            selectedMeal = meal
        )
    }
}