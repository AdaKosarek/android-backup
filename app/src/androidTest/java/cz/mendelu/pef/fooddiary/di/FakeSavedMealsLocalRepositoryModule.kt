package cz.mendelu.pef.fooddiary.di
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.fake.FakeSavedMealsLocalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class FakeSavedMealsLocalRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindSavedMealsRepository(
        impl: FakeSavedMealsLocalRepository
    ): ISavedMealsLocalRepository
}
