package cz.mendelu.pef.fooddiary.di

import android.content.Context
import cz.mendelu.pef.fooddiary.database.SavedMealsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SavedMealsDatabase {
        return SavedMealsDatabase.getDatabase(context)
    }

}