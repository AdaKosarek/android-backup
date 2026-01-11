package cz.mendelu.pef.fooddiary.di
import cz.mendelu.pef.fooddiary.fake.FakeImageStorageRepository
import cz.mendelu.pef.fooddiary.utils.IImageStorageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ImageStorageModule::class]
)
abstract class FakeImageStorageModule {

    @Binds
    abstract fun bindImageStorageRepository(
        impl: FakeImageStorageRepository
    ): IImageStorageRepository
}
