package cz.petstore2025.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun returnBack()

    fun navigateToPetDetail(petId: Long)


    fun getCurrentSavedStateHandle(): SavedStateHandle?
}