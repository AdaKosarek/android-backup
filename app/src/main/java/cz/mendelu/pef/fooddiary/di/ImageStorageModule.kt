package cz.mendelu.pef.fooddiary.di
import cz.mendelu.pef.fooddiary.utils.IImageStorageRepository
import cz.mendelu.pef.fooddiary.utils.ImageStorageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
abstract class ImageStorageModule {

    @Binds
    @Singleton
    abstract fun bindImageStorageRepository(
        impl: ImageStorageRepository
    ): IImageStorageRepository
}
