package cz.mendelu.pef.fooddiary.di
import cz.mendelu.pef.fooddiary.communication.FoodsAPI
import cz.mendelu.pef.fooddiary.communication.FoodsRemoteRepositoryImpl
import cz.mendelu.pef.fooddiary.communication.IFoodsRemoteRepository
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
    fun provideFoodsRemoteRepository(foodsAPI: FoodsAPI): IFoodsRemoteRepository =
        FoodsRemoteRepositoryImpl(foodsAPI)
}