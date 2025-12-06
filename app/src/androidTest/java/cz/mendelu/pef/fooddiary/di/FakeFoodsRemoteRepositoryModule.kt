package cz.mendelu.pef.fooddiary.di

import cz.mendelu.pef.fooddiary.communication.IFoodsRemoteRepository
import cz.mendelu.pef.fooddiary.fake.FakeFoodsRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteRepositoryModule::class],
)
abstract class FakeFoodsRemoteRepositoryModule {
    @Binds
    abstract fun provideFoodsRemoteRepository(service: FakeFoodsRemoteRepositoryImpl): IFoodsRemoteRepository
}
