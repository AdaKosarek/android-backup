package cz.mendelu.pef.fooddiary.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cz.mendelu.pef.fooddiary.ui.screens.detail.FoodDetailScreen
import cz.mendelu.pef.fooddiary.ui.screens.discover.DiscoverScreen


@ExperimentalFoundationApi
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable(Destination.DiscoverScreen.route) {
            DiscoverScreen(navigation = navigation)
        }

        composable<FoodDetailDestination> { backStackEntry ->
            val destination = backStackEntry.toRoute<FoodDetailDestination>()
            FoodDetailScreen(
                navigation = navigation,
                destination = destination
            )

        }

    }
}
