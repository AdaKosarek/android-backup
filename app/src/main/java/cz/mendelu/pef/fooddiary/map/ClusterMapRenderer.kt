package cz.mendelu.pef.fooddiary.map

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import cz.mendelu.pef.fooddiary.model.SavedMealSource

class ClusterMapRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<SavedMealClusterItem>
) : DefaultClusterRenderer<SavedMealClusterItem>(context, map, clusterManager) {

    override fun shouldRenderAsCluster(cluster: Cluster<SavedMealClusterItem>): Boolean {
        return cluster.size >= 4
    }

    override fun onBeforeClusterItemRendered(
        item: SavedMealClusterItem,
        markerOptions: MarkerOptions
    ) {
        super.onBeforeClusterItemRendered(item, markerOptions)

        val hue = when {
            item.meal.isFavorite -> BitmapDescriptorFactory.HUE_ORANGE
            item.meal.source == SavedMealSource.FAB -> BitmapDescriptorFactory.HUE_AZURE
            item.meal.source == SavedMealSource.API_PLUS_FAB -> BitmapDescriptorFactory.HUE_AZURE
            else -> BitmapDescriptorFactory.HUE_ROSE
        }

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(hue))
    }
}
