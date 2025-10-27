package cz.petstore2025.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import cz.petstore2025.model.Pet
import cz.petstore2025.navigation.INavigationRouter
import cz.petstore2025.navigation.PetDetailDestination
import cz.petstore2025.ui.elements.BaseScreen
import cz.petstore2025.ui.elements.PlaceholderScreenContent
import cz.petstore2025.R
import kotlinx.coroutines.delay

@Composable
fun PetDetailScreen(
    navigation: INavigationRouter,
    destination: PetDetailDestination,
    viewModel: PetDetailViewModel = hiltViewModel()
){

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        viewModel.loadPetDetail(destination.petId)
    }

    //zpět po smazani
    LaunchedEffect(state.value.deletionSuccess) {
        if (state.value.deletionSuccess) {
            navigation.getNavController()
                .previousBackStackEntry
                ?.savedStateHandle
                ?.set("refreshList", true)

            delay(100)
            navigation.returnBack()
        }
    }

    BaseScreen(
        topBarText = "Detail",
        onBackClick = { navigation.returnBack() },
        showLoading = state.value.loading,
        placeholderScreenContent = when {//
            state.value.error != null -> PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(state.value.error!!)
            )
            !state.value.loading && state.value.pet == null -> PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(R.string.no_detail_data)
            )
            else -> null
        },
        actions = {
            // tlacitko smazani
            IconButton(onClick = { viewModel.deletePet(destination.petId) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_pet)
                )
            }
        }
    ) {
        PetDetailScreenContent(
            paddingValues = it,
            pet = state.value.pet
        )
    }

    // chybový dialog pro smazání
    state.value.deletionError?.let { errRes ->
        AlertDialog(
            onDismissRequest = {
                viewModel.clearDeletionError()
            },
            confirmButton = {
                TextButton(onClick = { viewModel.clearDeletionError() }) {
                    Text(stringResource(R.string.ok))
                }
            },
            title = {
                Text("Delete failed title")
            },
            text = {
                Text(stringResource(errRes))
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
    pet: Pet?
) {
    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(16.dp)
    ) {
        pet?.let {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Name: ${it.name ?: "-"}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "ID: ${it.id}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Category: ${it.category?.name ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Status: ${it.status ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tags: ${it.tags?.joinToString { tag -> tag.name ?: "-" } ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                it.photoUrls?.let { urls ->
                    if (urls.isNotEmpty()) {
                        // zobrazíme jen první fotku jako ukázku
                        Image(
                            painter = rememberAsyncImagePainter(urls.first()),
                            contentDescription = it.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No details available", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

