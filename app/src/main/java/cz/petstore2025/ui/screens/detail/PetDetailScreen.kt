package cz.petstore2025.ui.screens.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.petstore2025.model.Pet
import cz.petstore2025.navigation.INavigationRouter
import cz.petstore2025.navigation.PetDetailDestination
import cz.petstore2025.ui.elements.BaseScreen
import cz.petstore2025.ui.screens.list.ListOfPetsViewModel

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

    BaseScreen(
        topBarText = "Detail",
        onBackClick = {
            navigation.returnBack()
        }
    ) {
        PetDetailScreenContent(
            paddingValues = it,
            pet = state.value.pet
        )
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
    pet: Pet?
){

}
