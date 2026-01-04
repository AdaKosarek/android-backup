package cz.mendelu.pef.fooddiary.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import cz.mendelu.pef.fooddiary.database.SavedMeal

data class SavedMealClusterItem(
    val meal: SavedMeal
) : ClusterItem {

    override fun getPosition(): LatLng =
        LatLng(
            meal.latitude ?: 0.0,
            meal.longitude ?: 0.0
        )

    override fun getTitle(): String = meal.customName?.takeIf { it.isNotBlank() }
            ?: meal.title.orEmpty()

    override fun getSnippet(): String = meal.placeName?.takeIf { it.isNotBlank() }.orEmpty()

    override fun getZIndex(): Float? = 0f
}

