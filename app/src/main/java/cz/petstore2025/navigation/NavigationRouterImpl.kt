package cz.petstore2025.navigation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun getNavController(): NavController = navController

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToPetDetail(petId: Long) {
        navController.navigate(PetDetailDestination(petId))
    }

    override fun navigateToAddPet() {
        navController.navigate(Destination.AddPetScreen.route)
    }

    //pro reload
    override fun getCurrentSavedStateHandle(): SavedStateHandle? =
        navController.currentBackStackEntry?.savedStateHandle

    override fun navigateToLogin() {
        navController.navigate(Destination.LoginScreen.route) {
            popUpTo(0) { inclusive = true }
        }
    }
}