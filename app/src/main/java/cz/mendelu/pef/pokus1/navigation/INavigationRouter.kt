package cz.mendelu.pef.pokus1.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun returnBack()
    fun navigateToStopDetail(stopId: Long)
    fun navigateToMap()
}
