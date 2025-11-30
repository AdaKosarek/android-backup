package cz.mendelu.pef.fooddiary.di

import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.database.SavedMealsDao
import cz.mendelu.pef.fooddiary.database.SavedMealsLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: SavedMealsDao): ISavedMealsLocalRepository {
        return SavedMealsLocalRepositoryImpl(dao)
    }

}