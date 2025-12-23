package cz.mendelu.pef.fooddiary

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.fooddiary.mock.ServerMock
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.NavGraph
import cz.mendelu.pef.fooddiary.ui.activities.MainActivity
import cz.mendelu.pef.fooddiary.ui.theme.FoodDiaryTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsFoodDetail {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_detail_screen_is_displayed() {
        launchDetailScreen()

        composeRule.waitForIdle()
        Thread.sleep(500)

        composeRule.onNodeWithTag("TestTagDetailTitle")
            .assertIsDisplayed()
            .assertTextEquals(ServerMock.recipeDetail.title!!)

        composeRule.onNodeWithTag("TestTagDetailNutrition")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun test_click_back_navigates_back_to_discover() {
        launchDetailScreen()

        composeRule.waitForIdle()
        Thread.sleep(400)

        composeRule.onNodeWithTag("TestTagDetailBackButton")
            .assertIsDisplayed()
            .performClick()

        composeRule.waitForIdle()
        Thread.sleep(400)

        composeRule.onNodeWithTag("TestTagDiscoverList")
            .assertIsDisplayed()
    }

    @Test
    fun test_scroll_to_ingredients_and_instructions() {
        launchDetailScreen()

        composeRule.waitForIdle()
        Thread.sleep(400)

        composeRule.onNodeWithTag("TestTagDetailContainer")
            .performScrollToNode(hasTestTag("TestTagDetailIngredient"))

        composeRule.onNodeWithTag("TestTagDetailIngredientsList")
            .performScrollToNode(hasText(ServerMock.recipeDetail.extendedIngredients!!.first().name!!))

        composeRule.onNodeWithTag("TestTagDetailContainer")
            .performScrollToNode(hasTestTag("TestTagDetailInstruction"))

        composeRule.onNodeWithTag("TestTagDetailInstruction")
            .assertExists()
    }

    @Test
    fun test_favorite_button_toggles() {
        launchDetailScreen()

        composeRule.waitForIdle()
        Thread.sleep(400)

        val fav = composeRule.onNodeWithTag("TestTagDetailFavorite")

        fav.assertIsDisplayed()
        fav.performClick()

        composeRule.waitForIdle()
        Thread.sleep(300)

        fav.assertIsDisplayed()
    }

    @OptIn(ExperimentalFoundationApi::class)
    private fun launchDetailScreen() {
        val fakeId = ServerMock.recipeDetail.id

        composeRule.activity.setContent {
            FoodDiaryTheme {
                navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = Destination.DiscoverScreen.route
                )

                LaunchedEffect(Unit) {
                    navController.navigate(FoodDetailDestination(fakeId))
                }
            }
        }
    }
}

