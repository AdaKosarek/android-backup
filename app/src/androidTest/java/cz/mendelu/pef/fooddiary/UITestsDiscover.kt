package cz.mendelu.pef.fooddiary

import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsDiscover {

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
    fun test_discover_screen_is_displayed() {
        launchDiscoverScreen()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("TestTagDiscoverList")
            .assertExists()
            .assertIsDisplayed()
    }

    //ověří scrollování k poslednímu mock receptu
    @Test
    fun test_scroll_list_and_find_last_item() {
        launchDiscoverScreen()
        composeRule.waitForIdle()

        val targetRecipe = ServerMock.allRecipes.results!!.last()
        val targetTitle = targetRecipe.title!!

        composeRule.onNodeWithTag("TestTagDiscoverList")
            .performScrollToNode(hasText(targetTitle))
        composeRule.waitForIdle()
        composeRule.onNode(hasText(targetTitle))
            .assertIsDisplayed()
    }

    //klikne na první recept a musí navigovat na detail
    @Test
    fun test_click_first_item_and_navigate_to_detail() {
        launchDiscoverScreen()

        val first = ServerMock.allRecipes.results!!.first()
        val title = first.title!!
        val id = first.id

        composeRule.onNodeWithText(title).performClick()
        composeRule.waitForIdle()

        // kontrola navigace podle route argumentu
        val entry = navController.currentBackStackEntry
        val destination = entry?.savedStateHandle?.get<Long>("foodId")

        assertEquals(id, destination)
    }

    @Test
    fun test_scroll_click_and_navigate_back() {
        launchDiscoverScreen()
        val target = ServerMock.allRecipes.results!!.last()
        val title = target.title!!

        composeRule.onNodeWithTag("TestTagDiscoverList")
            .performScrollToNode(hasText(title))

        composeRule.onNodeWithText(title).performClick()
        composeRule.waitForIdle()

        val route = navController.currentBackStackEntry?.destination?.route
        assertTrue(route?.contains("foodId") == true)

        composeRule.onNodeWithContentDescription("Back").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("TestTagDiscoverList").assertIsDisplayed()
    }
    //launcher DiscoverScreen
    @OptIn(ExperimentalFoundationApi::class)
    private fun launchDiscoverScreen() {
        composeRule.activity.setContent {
            FoodDiaryTheme {
                navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = Destination.DiscoverScreen.route
                )
            }
        }
    }
}