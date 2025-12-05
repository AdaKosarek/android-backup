package cz.mendelu.pef.fooddiary.ui.screens.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.fooddiary.navigation.FoodDetailDestination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen

@Composable
fun FoodDetailScreen(
    navigation: INavigationRouter,
    destination: FoodDetailDestination,
    viewModel: FoodDetailViewModel = hiltViewModel()
){


    BaseScreen(
        topBarText = "Detail",
        onBackClick = {
            navigation.returnBack()
        }
    ) {
        PetDetailScreenContent(
            paddingValues = it,
        )
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
){

}