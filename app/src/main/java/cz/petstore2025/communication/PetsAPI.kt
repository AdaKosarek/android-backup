package cz.petstore2025.communication

import cz.petstore2025.model.Pet
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PetsAPI {

    @Headers("Content-Type: application/json")
    @GET("pet/findByStatus")
    suspend fun findByStatus(@Query("status") status: String): Response<List<Pet>>

    @Headers("Content-Type: application/json")
    @GET("pet/{petId}")
    suspend fun findPetById(
        @Path("petId") petId: Long
    ): Response<Pet>

    //vymazani
    @Headers("Content-Type: application/json")
    @DELETE("pet/{petId}")
    suspend fun deletePet(
        @Path("petId") petId: Long
    ): Response<Unit>
}