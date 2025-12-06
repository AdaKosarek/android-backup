package cz.mendelu.pef.fooddiary.navigation

sealed class Destination(
    val route: String
){
    object DiscoverScreen : Destination(route = "discover")

}
