package cz.mendelu.pef.fooddiary.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.pef.fooddiary.ui.screens.addmealform.AddMealFormScreen
import cz.mendelu.pef.fooddiary.ui.screens.detail.FoodDetailScreen
import cz.mendelu.pef.fooddiary.ui.screens.discover.DiscoverScreen
import cz.mendelu.pef.fooddiary.ui.screens.map.MapScreen
import cz.mendelu.pef.fooddiary.ui.screens.saved.SavedScreen
import cz.mendelu.pef.fooddiary.ui.screens.saveddetail.SavedDetailScreen
import cz.mendelu.pef.fooddiary.ui.screens.search.SearchScreen


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

        composable(Destination.DiscoverScreen.route) {
            DiscoverScreen(navigation)
        }

        //apidetail
        composable(
            route = "${Destination.FoodDetailScreen.route}/{foodId}",
            arguments = listOf(
                navArgument("foodId") {
                    type = NavType.LongType
                }
            )
        ) {
            val foodId = it.arguments?.getLong("foodId") ?: -1L
            FoodDetailScreen(navigation, foodId)
        }

        //addmeal
        composable(
            route = "${Destination.AddMealFormScreen.route}/{apiId}",
            arguments = listOf(
                navArgument("apiId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { backStackEntry ->
            val apiId = backStackEntry
                .arguments
                ?.getLong("apiId")
                ?.takeIf { it != -1L }

            AddMealFormScreen(
                navigation = navigation,
                apiId = apiId
            )
        }

        //saveddetail
        composable(
            route = "${Destination.SavedDetailScreen.route}/{localId}",
            arguments = listOf(
                navArgument("localId") {
                    type = NavType.LongType
                }
            )
        ) {
            val localId = it.arguments?.getLong("localId") ?: -1L
            SavedDetailScreen(
                navigation = navigation,
                localId = localId
            )
        }

        composable(Destination.SavedScreen.route) {
            SavedScreen(navigation)
        }

        composable(Destination.SearchScreen.route) {
            SearchScreen(navigation)
        }

        composable(Destination.MapScreen.route) {
            MapScreen(navigation)
        }

    }
}
