package cz.petstore2025.di

import android.content.Context
import cz.petstore2025.communication.IPetsRemoteRepository
import cz.petstore2025.communication.PetsAPI
import cz.petstore2025.communication.PetsRemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun providePetsRemoteRepository(petsAPI: PetsAPI, @ApplicationContext context: Context): IPetsRemoteRepository =
        PetsRemoteRepositoryImpl(petsAPI, context)
}