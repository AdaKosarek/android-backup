package cz.mendelu.pef.pokus1.communication.api
import cz.mendelu.pef.pokus1.communication.CommunicationResult
import cz.mendelu.pef.pokus1.model.StopResponse
import javax.inject.Inject

class StopsRemoteRepositoryImpl @Inject constructor(
    private val api: StopsAPI
) : IStopsRemoteRepository {

    override suspend fun getStops(): CommunicationResult<StopResponse> {
        return processResponse {
            api.getStops()
        }
    }
}
