package cz.mendelu.pef.pokus1.ui.screens.map

/*
@Composable
fun MapScreen(
    navigation: INavigationRouter,
    viewModel: MapScreenViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = stringResource(R.string.map),
        onBackClick = { navigation.returnBack() },
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

        MapScreenContent(
            paddingValues = paddingValues,
            state = state.value
        )
    }
}

@Composable
fun MapScreenContent(
    paddingValues: PaddingValues,
    state: MapScreenUIState
) {
    val cameraPositionState = rememberCameraPositionState()
    var cameraInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(state.initialCameraPosition) {
        if (cameraInitialized) return@LaunchedEffect

        val target =
            state.initialCameraPosition ?: LatLng(49.8175, 15.4730)

        cameraPositionState.position =
            CameraPosition.fromLatLngZoom(
                target,
                if (state.initialCameraPosition != null) 11f else 7f
            )

        cameraInitialized = true
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        cameraPositionState = cameraPositionState
    ) {
        state.stops.forEach { stop ->

            val position = LatLng(
                stop.latitude!!,
                stop.longitude!!
            )

            val markerState = remember {
                MarkerState(position = position)
            }

            Marker(
                state = markerState,
                title = stop.name.orEmpty(),
                snippet = listOfNotNull(
                    stop.street,
                    stop.city
                ).joinToString(", ")
            )
        }
    }
}
*/
