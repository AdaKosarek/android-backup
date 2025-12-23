package cz.mendelu.pef.fooddiary.navigation
import androidx.navigation.NavController

interface INavigationRouter {
    fun navigateTo(destination: Destination)
    fun returnBack()
    fun navigateToFoodDetail(foodId: Long)
}