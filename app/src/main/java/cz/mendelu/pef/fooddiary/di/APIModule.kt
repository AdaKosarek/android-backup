package cz.mendelu.pef.fooddiary.di
import cz.mendelu.pef.fooddiary.communication.FoodsAPI
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
    fun provideFoodsAPI(retrofit: Retrofit): FoodsAPI =
        retrofit.create(FoodsAPI::class.java)
}