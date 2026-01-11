package cz.mendelu.pef.fooddiary.mock

import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.model.SavedMealSource
import kotlin.Double

object SavedServerMock {

    val savedFab = SavedMeal(
        source = SavedMealSource.FAB,
        localId = 1L,
        customName = "My Lunch",
        latitude = 49.1951,
        longitude = 16.6068,
        title = null,
        apiImage = null,
        readyInMinutes = null,
        servings = null,
        dishTypes = null,
        nutrition = null,
        extendedIngredients = null,
        instructions = null,
        analyzedInstructions = null,
        isFavorite = false,
        savedTimestamp = 1_700_000_000_000
    )

    val savedFavorite = savedFab.copy(
        localId = 2L,
        customName = "Favorite Meal",
        latitude = 49.8209,
        longitude = 18.2625,
        isFavorite = true
    )

    val savedApi = SavedMeal(
        source = SavedMealSource.API_ONLY,
        localId = 3L,
        apiId = 101L,
        title = "Zucchini Lasagna",
        apiImage = "https://spoonacular.com/recipeImages/715538-556x370.jpg",
        readyInMinutes = 35,
        servings = 4,
        dishTypes = listOf("lunch"),
        nutrition = null,
        extendedIngredients = null,
        instructions = null,
        analyzedInstructions = null,
        savedTimestamp = 1_700_000_000_500
    )

    val all = listOf(savedFab, savedFavorite, savedApi)
/*
    val newSavedFromForm = SavedMeal(
        source = SavedMealSource.FAB,
        localId = 999L,
        customName = "Stored Meal",
        title = null,
        apiImage = null,
        readyInMinutes = null,
        servings = null,
        dishTypes = null,
        nutrition = null,
        extendedIngredients = null,
        instructions = null,
        analyzedInstructions = null,
        savedTimestamp = 1_800_000_000_000
    )*/
}
