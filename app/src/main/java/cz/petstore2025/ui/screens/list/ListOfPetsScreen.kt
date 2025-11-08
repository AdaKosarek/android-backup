package cz.petstore2025.ui.screens.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.petstore2025.R
import cz.petstore2025.model.Pet
import cz.petstore2025.navigation.INavigationRouter
import cz.petstore2025.ui.elements.BaseScreen
import cz.petstore2025.ui.elements.PetRow
import cz.petstore2025.ui.elements.PlaceholderScreenContent



@Composable
fun ListOfPetsScreen(
    navigation: INavigationRouter,
    viewModel: ListOfPetsViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val navController = navigation.getNavController()
    val listState = rememberLazyListState()

    //reload
    LaunchedEffect(Unit) {
        navController.currentBackStackEntryFlow.collect { entry ->
            val handle = entry.savedStateHandle
            handle.getStateFlow("refreshList", false).collect { shouldRefresh ->
                if (shouldRefresh) {
                    viewModel.reloadPets()
                    handle["refreshList"] = false
                }
            }


        }
    }
    BaseScreen(
        topBarText = stringResource(R.string.list_of_pets),
        showLoading = state.value.loading,
        placeholderScreenContent = if (state.value.error != null) {
            PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(state.value.error!!.communicationError)
            )
        } else null,
        floatingActionButton = {
            FloatingActionButton(onClick = { navigation.navigateToAddPet() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_pet)
                )
            }
        }
    ) {
        ListOfPetsScreenContent(
            paddingValues = it,
            navigation = navigation,
            pets = state.value.pets,
            listState = listState
        )
    }
}

@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    pets: List<Pet>? = null,
    listState: LazyListState
) {

    pets?.let { petsList ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(petsList) { pet ->
                PetRow(
                    pet = pet,
                    onClick = { pet.id?.let {
                        navigation.navigateToPetDetail(it)
                    } }
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(start = 72.dp)
                )
            }
        }
    }
}








