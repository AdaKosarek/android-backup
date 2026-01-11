package cz.mendelu.pef.fooddiary.fake

import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.mock.SavedServerMock
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map


class FakeSavedMealsLocalRepository @Inject constructor() :
    ISavedMealsLocalRepository {

    private val mealsFlow = MutableStateFlow<List<SavedMeal>>(
        SavedServerMock.all
    )
    val currentMeals: List<SavedMeal>
        get() = mealsFlow.value

    override suspend fun insert(savedMeal: SavedMeal) {
        mealsFlow.value = mealsFlow.value + savedMeal
    }

    override fun getAll(): Flow<List<SavedMeal>> = mealsFlow

    override suspend fun update(savedMeal: SavedMeal) {
        mealsFlow.value = mealsFlow.value.map {
            if (it.localId == savedMeal.localId) savedMeal else it
        }
    }

    override suspend fun delete(savedMeal: SavedMeal) {
        mealsFlow.value = mealsFlow.value.filterNot {
            it.localId == savedMeal.localId
        }
    }

    override suspend fun getById(localId: Long): SavedMeal =
        mealsFlow.value.first { it.localId == localId }

    override fun getAllForMap(): Flow<List<SavedMeal>> =
        mealsFlow.map { it.filter { it.hasLocation } }

    override suspend fun deleteAll() {
        mealsFlow.value = emptyList()
    }
}

