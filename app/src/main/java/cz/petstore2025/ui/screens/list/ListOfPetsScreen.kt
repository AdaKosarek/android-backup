package cz.petstore2025.ui.screens.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import cz.petstore2025.navigation.INavigationRouter
import cz.petstore2025.ui.elements.BaseScreen
import cz.petstore2025.ui.theme.basicMargin


@Composable
fun ListOfPetsScreen(
    navigation: INavigationRouter,
    viewModel: ListOfPetsViewModel = hiltViewModel()

){

    BaseScreen(
        topBarText = "List of pets",
        floatingActionButton = {

        }
    ) {
        ListOfPetsScreenContent(
            paddingValues = it,
            navigation = navigation
        )
    }

}

@Composable
fun ListOfPetsScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
){
    Column(
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = basicMargin(),
                end = basicMargin()
            )
    ) {
        Text(text = "Testovaci text")
        Button(onClick = {
            navigation.navigateToPetDetail()
        }) {
            Text("Jdi na detail")
        }
    }

}









