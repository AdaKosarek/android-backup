package cz.mendelu.pef.pokus1.ui.screens.map
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import cz.mendelu.pef.pokus1.navigation.INavigationRouter
import cz.mendelu.pef.pokus1.ui.elements.BaseScreen
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMap
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.GridBasedAlgorithm
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import cz.mendelu.pef.pokus1.map.ClusterMapRenderer
import cz.mendelu.pef.pokus1.map.StopClusterItem
import cz.mendelu.pef.pokus1.model.Stop


@Composable
fun MapScreen(
    navigation: INavigationRouter,
    viewModel: MapViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "Map",
        onBackClick = { navigation.returnBack() },
        showLoading = state.value.loading,
    ) { paddingValues ->

        MapScreenContent(
            paddingValues = paddingValues,
            state = state.value,
            onStopSelected = viewModel::onStopSelected
        )
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreenContent(
    paddingValues: PaddingValues,
    state: MapScreenUIState,
    onStopSelected: (Stop?) -> Unit
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    var cameraInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(state.initialCameraPosition) {
        if (cameraInitialized) return@LaunchedEffect

        val target =
            state.initialCameraPosition ?: LatLng(49.8175, 15.4730)

        cameraPositionState.position =
            CameraPosition.fromLatLngZoom(
                target,
                if (state.initialCameraPosition != null) 12f else 7f
            )

        cameraInitialized = true
    }

    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var clusterManager by remember { mutableStateOf<ClusterManager<StopClusterItem>?>(null) }
    var clusterRenderer by remember { mutableStateOf<ClusterMapRenderer?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {

            MapEffect(state.stops) { map ->

                if (googleMap == null) googleMap = map
                if (clusterManager == null)
                    clusterManager = ClusterManager(context, map)

                if (clusterRenderer == null)
                    clusterRenderer = ClusterMapRenderer(
                        context,
                        map,
                        clusterManager!!
                    )

                clusterManager?.apply {
                    algorithm = GridBasedAlgorithm()
                    renderer = clusterRenderer
                    clearItems()
                    addItems(state.stops)
                    cluster()
                }

                clusterManager?.setOnClusterItemClickListener { item ->
                    onStopSelected(item.stop)
                    true
                }

                map.setOnCameraIdleListener {
                    clusterManager?.cluster()
                }
            }
        }

        state.selectedStop?.let {
            SelectedStopCard(
                stop = it,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                onDismiss = { onStopSelected(null) }
            )
        }
    }
}

@Composable
fun SelectedStopCard(
    stop: Stop,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = stop.name.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = listOfNotNull(
                        stop.street,
                        stop.city,
                        stop.postalCode
                    ).joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
    }
}

