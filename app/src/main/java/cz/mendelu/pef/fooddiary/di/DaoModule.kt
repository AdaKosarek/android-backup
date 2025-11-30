package cz.mendelu.pef.fooddiary.di

import cz.mendelu.pef.fooddiary.database.SavedMealsDao
import cz.mendelu.pef.fooddiary.database.SavedMealsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(savedMealsDatabase: SavedMealsDatabase): SavedMealsDao {
        return savedMealsDatabase.savedMealsDao()
    }

}