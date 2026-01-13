package cz.mendelu.pef.pokus1.ui.screens.list
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.pokus1.R
import cz.mendelu.pef.pokus1.model.Stop
import cz.mendelu.pef.pokus1.navigation.Destination
import cz.mendelu.pef.pokus1.navigation.INavigationRouter
import cz.mendelu.pef.pokus1.ui.elements.BaseScreen
import cz.mendelu.pef.pokus1.ui.elements.PlaceholderScreenContent

@Composable
fun ListScreen(
    navigation: INavigationRouter,
    viewModel: ListViewModel = hiltViewModel()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = stringResource(R.string.stops),
        showLoading = state.value.loading,
        actions = {
            IconButton(
                onClick = {
                    navigation.navigateToMap()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "map_icon"
                )
            }
        },
        /*
        actions = {
            TextButton(
                onClick = {
                    navigation.navigateTo(Destination.MapScreen)
                }
            ) {
                Text(text = stringResource(R.string.open_map))
            }
        }
        * */
        placeholderScreenContent =
            if (state.value.error != null) {
                PlaceholderScreenContent(
                    image = null,
                    title = null,
                    text = stringResource(state.value.error!!.communicationError)
                )
            } else null
    ) { paddingValues ->

        ListScreenContent(
            paddingValues = paddingValues,
            navigation = navigation,
            stops = state.value.stops
        )
    }
}

@Composable
fun ListScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    stops: List<Stop>? = null
) {

    stops?.let { stopList ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(stopList) { stop ->
                StopRow(
                    stop = stop,
                    onClick = {
                        stop.id?.let {
                            navigation.navigateToStopDetail(it)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StopRow(
    stop: Stop,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            stop.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            stop.street?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

