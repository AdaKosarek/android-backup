package cz.mendelu.pef.pokus1.communication.api1
import cz.mendelu.pef.pokus1.model.PackageDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface PackagesAPI {

    @Headers("Content-Type: application/json")
    @GET("packages.json")
    suspend fun getPackages(): Response<List<PackageDTO>>
}
