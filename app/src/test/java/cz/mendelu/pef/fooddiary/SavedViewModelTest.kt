package cz.mendelu.pef.fooddiary
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.model.SavedMealSource
import cz.mendelu.pef.fooddiary.ui.screens.saved.SavedScreenUIState
import cz.mendelu.pef.fooddiary.ui.screens.saved.SavedViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SavedViewModelTest {
    private val repository: ISavedMealsLocalRepository = mockk(relaxed = true)
    private lateinit var viewModel: SavedViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun when_repositoryEmitsMeals_uiStateIsSuccess() = runTest {
        val meals = listOf(
            SavedMeal(
                source = SavedMealSource.FAB,
                localId = 1L,
                customName = "Lunch",
                title = null,
                apiImage = null,
                readyInMinutes = null,
                servings = null,
                dishTypes = null,
                nutrition = null,
                extendedIngredients = null,
                instructions = null,
                analyzedInstructions = null,
                userNote = null,
                placeName = null,
                latitude = null,
                longitude = null,
                savedTimestamp = 123L
            )
        )

        val flow = MutableStateFlow(meals)
        every { repository.getAll() } returns flow

        viewModel = SavedViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is SavedScreenUIState.Success)
        assertEquals(meals, (state as SavedScreenUIState.Success).meals)
    }
}
