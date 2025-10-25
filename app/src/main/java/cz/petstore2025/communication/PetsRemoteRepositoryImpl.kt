package cz.petstore2025.communication

import cz.petstore2025.model.Pet
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject

class PetsRemoteRepositoryImpl @Inject constructor(private val api: PetsAPI) :
    IPetsRemoteRepository {


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
}
