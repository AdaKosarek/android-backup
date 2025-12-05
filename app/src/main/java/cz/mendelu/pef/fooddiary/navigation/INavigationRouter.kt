package cz.mendelu.pef.fooddiary.navigation
import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun returnBack()
    fun navigateToFoodDetail(foodId: Long)
}