package cz.petstore2025.communication

import cz.petstore2025.model.Pet
import retrofit2.Response
import javax.inject.Inject

class PetsRemoteRepositoryImpl @Inject constructor(private val api: PetsAPI) :
    IPetsRemoteRepository {


        override suspend fun findByStatus(status: String): CommunicationResult<List<Pet>> {
            try {
                val call: Response<List<Pet>> = api.findByStatus(status)
                if (call.isSuccessful){
                    if (call.body() != null){
                        // povedlo se a mam data
                        return CommunicationResult.Success(call.body()!!)
                    } else {
                        return CommunicationResult.Error(
                            CommunicationError(
                                code = call.code(),
                                message = call.errorBody().toString()
                            )
                        )
                    }
                } else {
                    return CommunicationResult.Error(
                        CommunicationError(
                            code = call.code(),
                            message = call.errorBody().toString()
                        )
                    )
                }
            } catch (exception: Exception){
                return CommunicationResult.Exception(exception)
            }

        }


}