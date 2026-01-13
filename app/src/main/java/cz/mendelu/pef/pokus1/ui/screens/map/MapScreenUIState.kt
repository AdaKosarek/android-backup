package cz.mendelu.pef.pokus1.ui.screens.map

import com.google.android.gms.maps.model.LatLng
import cz.mendelu.pef.pokus1.communication.CommunicationError
import cz.mendelu.pef.pokus1.map.StopClusterItem
import cz.mendelu.pef.pokus1.model.Stop

data class MapScreenUIState(
    val loading: Boolean = true,
    val stops: List<StopClusterItem> = emptyList(),
    val error: MapScreenError? = null,
    val selectedStop: Stop? = null,
    val initialCameraPosition: LatLng? = null
)
