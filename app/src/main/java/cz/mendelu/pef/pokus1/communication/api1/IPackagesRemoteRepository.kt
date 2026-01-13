package cz.mendelu.pef.pokus1.communication.api1

import cz.mendelu.pef.pokus1.communication.CommunicationResult
import cz.mendelu.pef.pokus1.communication.IBaseRemoteRepository
import cz.mendelu.pef.pokus1.model.PackageDTO

interface IPackagesRemoteRepository : IBaseRemoteRepository {

    suspend fun getPackages(): CommunicationResult<List<PackageDTO>>
}
