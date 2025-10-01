package cz.petstore2025.communication

import cz.petstore2025.model.Pet
import retrofit2.Response
import retrofit2.http.Query

interface IPetsRemoteRepository {

    suspend fun findByStatus(status: String): CommunicationResult<List<Pet>>

}