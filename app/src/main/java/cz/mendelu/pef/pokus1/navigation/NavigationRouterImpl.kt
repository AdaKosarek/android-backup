package cz.mendelu.pef.pokus1.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(
    private val navController: NavController
) : INavigationRouter {

    override fun getNavController(): NavController =
        navController

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToStopDetail(stopId: Long) {
        navController.navigate(
            StopDetailDestination(stopId)
        )
    }
    override fun navigateToMap() {
        navController.navigate(
            Destination.MapScreen.route
        )
    }
}
