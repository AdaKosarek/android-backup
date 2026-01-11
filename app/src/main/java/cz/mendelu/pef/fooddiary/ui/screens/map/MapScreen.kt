package cz.mendelu.pef.fooddiary.ui.screens.map
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.algo.GridBasedAlgorithm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.map.ClusterMapRenderer
import cz.mendelu.pef.fooddiary.map.SavedMealClusterItem
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin

@Composable
fun MapScreen(
    navigation: INavigationRouter,
    viewModel: MapScreenViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasLocationPermission = granted
        }

    val fusedLocationClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    BaseScreen(
        hideTopBar = true,
        currentDestination = Destination.MapScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        showLoading = state.value.loading
    ) { padding ->

        MapScreenContent(
            paddingValues = padding,
            state = state.value,
            hasLocationPermission = hasLocationPermission,
            requestLocationPermission = {
                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            },
            fusedLocationClient = fusedLocationClient,
            actions = viewModel,
            navigation = navigation
        )
    }
}


@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreenContent(
    paddingValues: PaddingValues,
    state: MapScreenUIState,
    hasLocationPermission: Boolean,
    requestLocationPermission: () -> Unit,
    fusedLocationClient: FusedLocationProviderClient,
    actions: MapScreenActions,
    navigation: INavigationRouter
) {
    val context = LocalContext.current
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }

    val cameraPositionState = rememberCameraPositionState()
    var cameraInitialized by remember { mutableStateOf(false) }

    @SuppressLint("MissingPermission")
    LaunchedEffect(Unit) {
        if (cameraInitialized) return@LaunchedEffect

        if (hasLocationPermission) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    val target = location?.let {
                        LatLng(it.latitude, it.longitude)
                    } ?: LatLng(49.8175, 15.4730)

                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(
                            target,
                            if (location != null) 13f else 9f
                        )

                    cameraInitialized = true
                }
                .addOnFailureListener {
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(
                            LatLng(49.8175, 15.4730),
                            9f
                        )
                    cameraInitialized = true
                }
        } else {
            //zadost
            requestLocationPermission()

            //fallback
            cameraPositionState.position =
                CameraPosition.fromLatLngZoom(
                    LatLng(49.8175, 15.4730),
                    9f
                )

            cameraInitialized = true
        }
    }

    var googleMap by remember { mutableStateOf<GoogleMap?>(null) }
    var clusterManager by remember { mutableStateOf<ClusterManager<SavedMealClusterItem>?>(null) }
    var clusterRenderer by remember { mutableStateOf<ClusterMapRenderer?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            uiSettings = uiSettings,
            cameraPositionState = cameraPositionState
        ) {

            MapEffect(state.meals) { map ->

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
                    addItems(state.meals)
                    cluster()
                }

                clusterManager?.setOnClusterItemClickListener { item ->
                    actions.onMealSelected(item.meal)
                    true
                }

                map.setOnCameraIdleListener {
                    clusterManager?.cluster()
                }
            }
        }

        state.selectedMeal?.let {
            SelectedMealCard(
                meal = it,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(basicMargin()),
                onDismiss = { actions.onMealSelected(null) },
                onOpenDetail = { localId ->
                    navigation.navigateToSavedDetail(localId)
                }
            )
        }
    }
}



@Composable
fun SelectedMealCard(
    meal: SavedMeal,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onOpenDetail: (Long) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
            .testTag("TestTagSelectedMealCard"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = meal.userPhotoUri
                    ?: meal.apiImage
                    ?: R.drawable.foods_common,
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = meal.customName
                        ?: meal.title.orEmpty(),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!meal.placeName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = meal.placeName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrayText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = stringResource(R.string.open_detail),
                    color = OrangePrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .testTag("TestTagOpenDetail")
                        .padding(top = 6.dp)
                        .clickable {
                            onOpenDetail(meal.localId)
                        }
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


