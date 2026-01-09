package cz.mendelu.pef.fooddiary.database

import cz.mendelu.pef.fooddiary.model.SavedMealSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedMealsLocalRepositoryImpl @Inject constructor(private val dao: SavedMealsDao) : ISavedMealsLocalRepository {

    override suspend fun insert(savedMeal: SavedMeal) {
        return dao.insert(savedMeal)
    }

    override fun getAll(): Flow<List<SavedMeal>> {
        return dao.getAll()
    }

    override suspend fun update(savedMeal: SavedMeal) {
        dao.update(savedMeal)
    }

    override suspend fun delete(savedMeal: SavedMeal) {
        dao.delete(savedMeal)
    }

    override suspend fun getById(localId: Long): SavedMeal {
        return dao.getById(localId)
    }

    override fun getAllForMap(): Flow<List<SavedMeal>> =
        dao.getAllForMap(excludedSource = SavedMealSource.API_ONLY)

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}