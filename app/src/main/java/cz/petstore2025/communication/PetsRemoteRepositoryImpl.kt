package cz.petstore2025.communication


import cz.petstore2025.model.Order
import cz.petstore2025.model.Pet
import java.time.Instant
import javax.inject.Inject


class PetsRemoteRepositoryImpl @Inject constructor(
    private val api: PetsAPI) : IPetsRemoteRepository {

    override suspend fun findByStatus(status: String): CommunicationResult<List<Pet>> {
        return processResponse {
            api.findByStatus(status)
        }

    }

    override suspend fun findPetById(petId: Long): CommunicationResult<Pet> {
        return processResponse {
            api.findPetById(petId)
        }
    }

    //vymazani
    override suspend fun deletePet(petId: Long): CommunicationResult<Unit> {
        return processResponse {
            api.deletePet(petId)
        }
    }

    //pridani zvirete
    override suspend fun addPet(pet: Pet): CommunicationResult<Pet> {
        return processResponse {
            api.addPet(pet)
        }
    }

    override suspend fun orderPet(petId: Long, quantity: Int): CommunicationResult<Order> {
        val newOrder = Order(
            petId = petId,
            quantity = quantity,
            shipDate = Instant.now().toString(),
            status = "placed",
            complete = false
        )

        return processResponse {
            api.placeOrder(newOrder)
        }
    }
}
