package cz.mendelu.pef.fooddiary.navigation

sealed class Destination(
    val route: String
){
    object DiscoverScreen : Destination(route = "discover")
    object SavedScreen : Destination("saved")
    object MapScreen : Destination("map")
    object SettingsScreen : Destination("settings")


    object FoodDetailScreen : Destination("food_detail")
    object AddOptionScreen : Destination("add_option")
}
