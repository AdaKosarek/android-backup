package cz.mendelu.pef.fooddiary.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SavedMeal::class], version = 1, exportSchema = true)
abstract class SavedMealsDatabase : RoomDatabase() {

    abstract fun savedMealsDao(): SavedMealsDao

    companion object {
        private var INSTANCE: SavedMealsDatabase? = null

        fun getDatabase(context: Context): SavedMealsDatabase {
            if (INSTANCE == null) {
                synchronized(SavedMealsDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            SavedMealsDatabase::class.java,
                            "saved_meals_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }


    }
}