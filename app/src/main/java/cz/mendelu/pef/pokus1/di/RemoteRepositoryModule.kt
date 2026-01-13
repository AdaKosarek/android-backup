package cz.mendelu.pef.pokus1.di
import cz.mendelu.pef.pokus1.communication.api.IStopsRemoteRepository
import cz.mendelu.pef.pokus1.communication.api.StopsAPI
import cz.mendelu.pef.pokus1.communication.api.StopsRemoteRepositoryImpl
import cz.mendelu.pef.pokus1.communication.api1.IPackagesRemoteRepository
import cz.mendelu.pef.pokus1.communication.api1.PackagesAPI
import cz.mendelu.pef.pokus1.communication.api1.PackagesRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun provideStopsRemoteRepository(stopsAPI: StopsAPI): IStopsRemoteRepository =
        StopsRemoteRepositoryImpl(stopsAPI)

    @Provides
    @Singleton
    fun providePackagesRemoteRepository(
        packagesAPI: PackagesAPI
    ): IPackagesRemoteRepository =
        PackagesRemoteRepositoryImpl(packagesAPI)
}