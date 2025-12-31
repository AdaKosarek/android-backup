package cz.mendelu.pef.fooddiary.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(
    private val navController: NavController
) : INavigationRouter {

    override fun navigateTo(destination: Destination) {
        val currentRoute = navController.currentBackStackEntry
            ?.destination
            ?.route

        if (currentRoute == destination.route) return

        val rootDestinations = listOf(
            Destination.DiscoverScreen,
            Destination.SavedScreen,
            Destination.MapScreen,
            Destination.SettingsScreen
        )

        if (destination in rootDestinations) {
            navController.navigate(destination.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
                restoreState = true
            }
        } else {
            navController.navigate(destination.route)
        }
    }

    override fun navigateToFoodDetail(foodId: Long) {
        navController.navigate(
            "${Destination.FoodDetailScreen.route}/$foodId"
        )
    }

    override fun navigateToAddMealForm(apiId: Long?) {
        val route = if (apiId == null) {
            "${Destination.AddMealFormScreen.route}/-1"
        } else {
            "${Destination.AddMealFormScreen.route}/$apiId"
        }

        navController.navigate(route)
    }

    override fun navigateToSavedDetail(localId: Long) {
        navController.navigate(
            "${Destination.SavedDetailScreen.route}/$localId"
        )
    }

    override fun returnBack() {
        navController.popBackStack()
    }
}