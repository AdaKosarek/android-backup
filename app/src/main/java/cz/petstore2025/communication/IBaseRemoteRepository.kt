package cz.petstore2025.communication

import cz.petstore2025.model.Pet
import retrofit2.Response
import java.io.InterruptedIOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface IBaseRemoteRepository {

    suspend fun <T: Any> processResponse(
        request: suspend () -> Response<T>
    ): CommunicationResult<T> {

        try {
            val call = request()
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
        } catch (unknownHostException: UnknownHostException) {
            return CommunicationResult.ConnectionError()
        } catch (socketEx: SocketTimeoutException){
            return CommunicationResult.ConnectionError()
        } catch (interruptedEx: InterruptedIOException){
            return CommunicationResult.ConnectionError()
        } catch (exception: Exception){
            return CommunicationResult.Exception(exception)
        }



    }
}