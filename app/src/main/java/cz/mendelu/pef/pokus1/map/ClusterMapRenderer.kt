package cz.mendelu.pef.pokus1.map
import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class ClusterMapRenderer(
    context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<StopClusterItem>
) : DefaultClusterRenderer<StopClusterItem>(
    context,
    map,
    clusterManager
) {

    override fun shouldRenderAsCluster(
        cluster: Cluster<StopClusterItem>
    ): Boolean = cluster.size >= 4
}