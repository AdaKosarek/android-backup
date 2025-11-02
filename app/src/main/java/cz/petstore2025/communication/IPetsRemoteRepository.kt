package cz.petstore2025.communication


import cz.petstore2025.model.ApiResponse
import cz.petstore2025.model.Order
import cz.petstore2025.model.Pet

interface IPetsRemoteRepository : IBaseRemoteRepository {

    suspend fun findByStatus(status: String): CommunicationResult<List<Pet>>
    suspend fun findPetById(petId: Long): CommunicationResult<Pet>

    //vymazani
    suspend fun deletePet(petId: Long): CommunicationResult<Unit>

    suspend fun addPet(pet: Pet): CommunicationResult<Pet>

    suspend fun orderPet(petId: Long, quantity: Int): CommunicationResult<Order>

    suspend fun loginUser(username: String, password: String): CommunicationResult<ApiResponse>

}