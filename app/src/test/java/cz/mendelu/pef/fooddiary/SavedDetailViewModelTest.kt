package cz.mendelu.pef.fooddiary
import cz.mendelu.pef.fooddiary.database.ISavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.model.SavedMealSource
import cz.mendelu.pef.fooddiary.ui.screens.saveddetail.SavedDetailViewModel
import cz.mendelu.pef.fooddiary.utils.ImageStorageRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SavedDetailViewModelTest {
    private val savedMealsRepository: ISavedMealsLocalRepository = mockk(relaxed = true)
    private val imageStorageRepository: ImageStorageRepository = mockk(relaxed = true)
    private lateinit var viewModel: SavedDetailViewModel

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        viewModel = SavedDetailViewModel(
            savedMealsRepository = savedMealsRepository,
            imageStorageRepository = imageStorageRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun when_editName_isBlank_editIsNotSaved() = runTest {
        val meal = SavedMeal(
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

        coEvery { savedMealsRepository.getById(1L) } returns meal

        viewModel.loadMeal(1L)
        advanceUntilIdle()

        viewModel.onToggleEdit()

        viewModel.onEditNameChange("")
        viewModel.onEditNoteChange("note")
        viewModel.onEditPlaceNameChange("Prague")

        viewModel.onToggleEdit()
        advanceUntilIdle()

        coVerify(exactly = 0) {
            savedMealsRepository.update(any())
        }

        val state = viewModel.uiState.value
        assertEquals(true, state.isEditing)
        assertEquals("Lunch", state.meal?.customName)
    }

    @Test
    fun when_editName_isValid_editIsSaved() = runTest {
        val meal = SavedMeal(
            source = SavedMealSource.FAB,
            localId = 1L,
            customName = "Lunch pasta",
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

        coEvery { savedMealsRepository.getById(1L) } returns meal

        viewModel.loadMeal(1L)
        advanceUntilIdle()

        viewModel.onToggleEdit()
        viewModel.onEditNameChange("Dinner")
        viewModel.onEditNoteChange("note")
        viewModel.onEditPlaceNameChange("Prague")

        viewModel.onToggleEdit()
        advanceUntilIdle()

        coVerify(exactly = 1) {
            savedMealsRepository.update(
                match {
                    it.customName == "Dinner" &&
                            it.placeName == "Prague"
                }
            )
        }

        val state = viewModel.uiState.value
        assertEquals(false, state.isEditing)
        assertEquals("Dinner", state.meal?.customName)
    }


    @Test
    fun toggleFavorite_changesValueAndSaves() = runTest {
        val meal = SavedMeal(
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
            isFavorite = false,
            savedTimestamp = 123L
        )

        coEvery { savedMealsRepository.getById(1L) } returns meal

        viewModel.loadMeal(1L)
        advanceUntilIdle()

        viewModel.onToggleFavorite()
        advanceUntilIdle()

        coVerify(exactly = 1) {
            savedMealsRepository.update(
                match { it.isFavorite }
            )
        }

        val state = viewModel.uiState.value
        assertEquals(true, state.meal?.isFavorite)
    }
}
