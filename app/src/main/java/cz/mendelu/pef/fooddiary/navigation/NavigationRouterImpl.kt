package cz.mendelu.pef.fooddiary.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {

    override fun getNavController(): NavController = navController

    override fun returnBack() {
        navController.popBackStack()
    }

    override fun navigateToFoodDetail(foodId: Long) {
        navController.navigate(FoodDetailDestination(foodId))
    }

}