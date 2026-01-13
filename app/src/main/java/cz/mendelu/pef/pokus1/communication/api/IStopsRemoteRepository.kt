package cz.mendelu.pef.pokus1.communication.api

import cz.mendelu.pef.pokus1.communication.CommunicationResult
import cz.mendelu.pef.pokus1.communication.IBaseRemoteRepository
import cz.mendelu.pef.pokus1.model.StopResponse

interface IStopsRemoteRepository : IBaseRemoteRepository {

    suspend fun getStops(): CommunicationResult<StopResponse>
}