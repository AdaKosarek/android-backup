package cz.petstore2025.navigation

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun getNavController(): NavController = navController

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToPetDetail() {
        navController.navigate(Destination.PetDetailScreen.route)
    }
}