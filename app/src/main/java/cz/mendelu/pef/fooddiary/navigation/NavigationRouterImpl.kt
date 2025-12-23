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

    override fun returnBack() {
        navController.popBackStack()
    }
}