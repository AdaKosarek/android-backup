package cz.mendelu.pef.fooddiary.database

import kotlinx.coroutines.flow.Flow

interface ISavedMealsLocalRepository {
    suspend fun insert(savedMeal: SavedMeal)
    fun getAll(): Flow<List<SavedMeal>>
    suspend fun update(savedMeal: SavedMeal)
    suspend fun delete(savedMeal: SavedMeal)
    suspend fun getById(localId: Long): SavedMeal
    fun getAllForMap(): Flow<List<SavedMeal>>

    suspend fun deleteAll()
}