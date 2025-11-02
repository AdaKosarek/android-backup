package cz.petstore2025.communication

import cz.petstore2025.model.ApiResponse
import cz.petstore2025.model.Order
import cz.petstore2025.model.Pet
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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


    @Headers("Content-Type: application/json")
    @POST("pet")
    suspend fun addPet(
        @Body pet: Pet
    ): Response<Pet>

    @Headers("Content-Type: application/json")
    @POST("store/order")
    suspend fun placeOrder(
        @Body order: Order
    ): Response<Order>

    @Headers("Content-Type: application/json")
    @GET("user/login")
    suspend fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<ApiResponse>

}