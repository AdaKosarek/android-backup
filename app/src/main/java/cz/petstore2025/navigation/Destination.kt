package cz.petstore2025.navigation

sealed class Destination(
    val route: String
){
    object ListOfPetsScreen : Destination(route = "list_of_pets")
    object PetDetailScreen : Destination(route = "detail")
    object AddPetScreen : Destination(route = "add_pet")
}
