package cz.petstore2025.ui.screens.detail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import cz.petstore2025.navigation.INavigationRouter
import cz.petstore2025.ui.elements.BaseScreen

@Composable
fun PetDetailScreen(
    navigation: INavigationRouter
){

    BaseScreen(
        topBarText = "Detail",
        onBackClick = {
            navigation.returnBack()
        }
    ) {
        PetDetailScreenContent(paddingValues = it)
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues
){

}
