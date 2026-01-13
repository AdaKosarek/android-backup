package cz.mendelu.pef.pokus1.navigation

sealed class Destination(val route: String){
    object ListScreen : Destination(route = "list")
    object DetailScreen : Destination(route = "detail")
    object MapScreen : Destination("map")
}