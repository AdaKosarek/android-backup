package cz.mendelu.pef.pokus1.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import cz.mendelu.pef.pokus1.R
import cz.mendelu.pef.pokus1.model.PackageDTO
import cz.mendelu.pef.pokus1.navigation.INavigationRouter
import cz.mendelu.pef.pokus1.navigation.StopDetailDestination
import cz.mendelu.pef.pokus1.ui.elements.BaseScreen
import cz.mendelu.pef.pokus1.ui.elements.PlaceholderScreenContent
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun DetailScreen(
    navigation: INavigationRouter,
    destination: StopDetailDestination,
    viewModel: DetailViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        viewModel.loadStopDetail(destination.stopId)
    }

    BaseScreen(
        topBarText = stringResource(R.string.detail),
        onBackClick = {
            navigation.returnBack()
        },
        showLoading = state.value.loading,
        placeholderScreenContent =
            state.value.error?.let {
                PlaceholderScreenContent(
                    image = null,
                    title = null,
                    text = stringResource(it.communicationError)
                )
            }
    ) { paddingValues ->

        DetailScreenContent(
            paddingValues = paddingValues,
            state = state.value
        )
    }
}

@Composable
fun DetailScreenContent(
    paddingValues: PaddingValues,
    state: DetailScreenUIState
) {

    val roundedWeight = BigDecimal(state.totalWeight)
        .setScale(1, RoundingMode.HALF_UP)
        .toDouble()
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
    ) {

        // ===== Informace o zastávce =====
        state.stop?.let { stop ->

            Text(
                text = stop.name ?: "",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            stop.street?.let {
                Text(text = it)
            }

            stop.city?.let {
                Text(text = it)
            }

            stop.postalCode?.let {
                Text(text = it)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ===== Agregované informace o balíčcích =====
        Text(
            text = "${stringResource(R.string.package_count)} ${state.packageCount}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${stringResource(R.string.total_weight)} $roundedWeight kg",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        //!PACKAGES
        if (state.packages.isNotEmpty()) {

            Text(
                text = "Packages for stop",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            PackagesList(
                packages = state.packages
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
        //

        state.stop?.let { stop ->
            if (stop.latitude != null && stop.longitude != null) {

                StopMap(
                    latitude = stop.latitude,
                    longitude = stop.longitude
                )
            }
        }
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun StopMap(
    latitude: Double,
    longitude: Double
) {
    val stopPosition = LatLng(latitude, longitude)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            stopPosition,
            15f
        )
    }
    val markerState = remember {
        MarkerState(position = stopPosition)
    }
    //val markerState = rememberMarkerState(position = stopPosition)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = false,
                myLocationButtonEnabled = false
            ),
            properties = MapProperties(
                isMyLocationEnabled = false
            )
        ) {
            Marker(
                state = markerState,
                title = stringResource(R.string.stop_location),
            )
        }
    }
}

//PACKAGES FOR STOP
@Composable
fun PackagesList(
    packages: List<PackageDTO>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            packages.forEachIndexed { index, pkg ->

                PackageRow(pkg)

                if (index != packages.lastIndex) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
fun PackageRow(
    pkg: PackageDTO
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.Inventory2,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = pkg.recipientName.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${pkg.weightKg ?: 0.0} kg",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
