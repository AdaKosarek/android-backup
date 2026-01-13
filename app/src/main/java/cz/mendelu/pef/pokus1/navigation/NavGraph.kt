package cz.mendelu.pef.pokus1.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import cz.mendelu.pef.pokus1.ui.screens.detail.DetailScreen
import cz.mendelu.pef.pokus1.ui.screens.list.ListScreen
import cz.mendelu.pef.pokus1.ui.screens.map.MapScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember {
        NavigationRouterImpl(navController)
    },
    startDestination: String
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Destination.ListScreen.route) {
            ListScreen(
                navigation = navigation
            )
        }

        composable<StopDetailDestination> { backStackEntry ->
            val destination =
                backStackEntry.toRoute<StopDetailDestination>()

            DetailScreen(
                navigation = navigation,
                destination = destination
            )
        }
        composable(Destination.MapScreen.route) {
            MapScreen(
                navigation = navigation
            )
        }
    }
}
