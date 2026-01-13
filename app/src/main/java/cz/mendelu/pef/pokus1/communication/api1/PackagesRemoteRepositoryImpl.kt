package cz.mendelu.pef.pokus1.communication.api1
import cz.mendelu.pef.pokus1.communication.CommunicationResult
import cz.mendelu.pef.pokus1.model.PackageDTO
import cz.mendelu.pef.pokus1.model.Stop
import javax.inject.Inject

class PackagesRemoteRepositoryImpl @Inject constructor(
    private val api: PackagesAPI
) : IPackagesRemoteRepository {

    override suspend fun getPackages(): CommunicationResult<List<PackageDTO>> {
        return processResponse {
            api.getPackages()
        }
    }
}
