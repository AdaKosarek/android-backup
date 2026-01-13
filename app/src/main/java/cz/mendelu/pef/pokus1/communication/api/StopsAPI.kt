package cz.mendelu.pef.pokus1.communication.api
import cz.mendelu.pef.pokus1.model.StopResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface StopsAPI{

    @Headers("Content-Type: application/json")
    @GET("stops.json")
    suspend fun getStops(): Response<StopResponse>
}
