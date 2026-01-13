package cz.mendelu.pef.pokus1.map
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import cz.mendelu.pef.pokus1.model.Stop

data class StopClusterItem(
    val stop: Stop
) : ClusterItem {

    override fun getPosition(): LatLng =
        LatLng(
            stop.latitude ?: 0.0,
            stop.longitude ?: 0.0
        )

    override fun getTitle(): String =
        stop.name.orEmpty()

    override fun getSnippet(): String =
        listOfNotNull(stop.street, stop.city)
            .joinToString(", ")

    override fun getZIndex(): Float? = 0f
}

