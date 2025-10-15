package cz.petstore2025.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cz.petstore2025.ui.screens.detail.PetDetailScreen
import cz.petstore2025.ui.screens.list.ListOfPetsScreen

@ExperimentalFoundationApi
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination){

        composable(Destination.ListOfPetsScreen.route) {
            ListOfPetsScreen(navigation = navigation)
        }

        composable<PetDetailDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<PetDetailDestination>()
            PetDetailScreen(
                navigation = navigation,
                destination = destination)

        }
    }
}
