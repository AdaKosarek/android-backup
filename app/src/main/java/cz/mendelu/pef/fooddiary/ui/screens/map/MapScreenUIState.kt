package cz.mendelu.pef.fooddiary.ui.screens.map

import com.google.android.gms.maps.model.LatLng
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.map.SavedMealClusterItem

data class MapScreenUIState(
    val loading: Boolean = true,
    val meals: List<SavedMealClusterItem> = emptyList(),
    val selectedMeal: SavedMeal? = null,
    val initialCameraPosition: LatLng? = null
)
