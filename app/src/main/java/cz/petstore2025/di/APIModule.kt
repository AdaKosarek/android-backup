package cz.petstore2025.di

import cz.petstore2025.communication.PetsAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun providePetsAPI(retrofit: Retrofit): PetsAPI =
        retrofit.create(PetsAPI::class.java)
}