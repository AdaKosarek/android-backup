package cz.mendelu.pef.fooddiary.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cz.mendelu.pef.fooddiary.model.SavedMealSource
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedMealsDao {

    @Insert
    suspend fun insert(savedMeal: SavedMeal)

    @Query("SELECT * FROM saved_meals ORDER BY savedTimestamp DESC")
    fun getAll(): Flow<List<SavedMeal>>

    @Query("SELECT * FROM saved_meals WHERE localId = :localId")
    suspend fun getById(localId: Long): SavedMeal

    @Update
    suspend fun update(savedMeal: SavedMeal)

    @Delete
    suspend fun delete(savedMeal: SavedMeal)

    @Query(
        """
        SELECT * FROM saved_meals
        WHERE source != :excludedSource
          AND latitude IS NOT NULL
          AND longitude IS NOT NULL
        ORDER BY savedTimestamp DESC
        """
    )
    fun getAllForMap(excludedSource: SavedMealSource = SavedMealSource.API_ONLY): Flow<List<SavedMeal>>
}