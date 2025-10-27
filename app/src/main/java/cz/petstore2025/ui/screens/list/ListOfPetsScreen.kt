package cz.petstore2025.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.petstore2025.model.Pet
import cz.petstore2025.navigation.INavigationRouter
import cz.petstore2025.ui.elements.BaseScreen
import cz.petstore2025.ui.elements.PlaceholderScreenContent
import cz.petstore2025.ui.theme.basicMargin


@Composable
fun ListOfPetsScreen(
    navigation: INavigationRouter,
    viewModel: ListOfPetsViewModel = hiltViewModel()

){

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val navController = navigation.getNavController()
    //reload po vymazu
    val currentBackStackEntry = navController.currentBackStackEntry

    LaunchedEffect(currentBackStackEntry) {
        currentBackStackEntry?.savedStateHandle
            ?.getLiveData<Boolean>("refreshList")
            ?.observeForever { shouldRefresh ->
                if (shouldRefresh == true) {
                    viewModel.reloadPets()
                    currentBackStackEntry.savedStateHandle["refreshList"] = false
                }
            }
    }


    BaseScreen(
        topBarText = "List of pets",
        showLoading = state.value.loading,
        placeholderScreenContent = if (state.value.error != null){
            PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(state.value.error!!.communicationError)
            )
        } else null,
        floatingActionButton = {

        }
    ) {
        ListOfPetsScreenContent(
            paddingValues = it,
            navigation = navigation,
            pets = state.value.pets
        )
    }

}

@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    pets: List<Pet>? = null
){

    pets?.let { petsList ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(items = petsList) {
                PetRow(it) {
                    navigation.navigateToPetDetail(it.id!!)
                }
            }
        }
    }
}

@Composable
fun PetRow(
    pet: Pet,
    onClick: () -> Unit
){
    Row(modifier = Modifier.fillMaxWidth().clickable{
        onClick()
    }) {
        pet.name?.let {
            Text(text = it)
        }
    }

}









