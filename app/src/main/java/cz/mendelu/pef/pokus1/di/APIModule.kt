package cz.mendelu.pef.pokus1.di

import cz.mendelu.pef.pokus1.communication.api.StopsAPI
import cz.mendelu.pef.pokus1.communication.api1.PackagesAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun provideStopsAPI(
        @Named("StopsRetrofit") retrofit: Retrofit
    ): StopsAPI =
        retrofit.create(StopsAPI::class.java)

    @Provides
    @Singleton
    fun providePackagesAPI(
        @Named("PackagesRetrofit") retrofit: Retrofit
    ): PackagesAPI =
        retrofit.create(PackagesAPI::class.java)
}