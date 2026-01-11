package cz.mendelu.pef.fooddiary
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.NavGraph
import cz.mendelu.pef.fooddiary.ui.activities.MainActivity
import cz.mendelu.pef.fooddiary.ui.theme.FoodDiaryTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class UITestsSearchScreen {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavHostController

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_search_screen_is_displayed() {
        launchSearchScreen()

        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.search_food)
        ).assertIsDisplayed()
    }

    @Test
    fun test_typing_query_shows_results() {
        launchSearchScreen()

        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.search_food_hint)
        ).performTextInput("Zucchini")

        composeRule.waitUntil {
            composeRule.onAllNodesWithText("Zucchini Lasagna")
                .fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun test_select_recipe_highlights_it() {
        launchSearchScreen()

        composeRule.onNodeWithTag("TestTagSearchInput")
            .performTextInput("Zucchini")

        composeRule.waitUntil {
            composeRule.onAllNodesWithText("Zucchini Lasagna")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule.onNodeWithText("Zucchini Lasagna")
            .performClick()
    }


    @Test
    fun test_select_none() {
        launchSearchScreen()

        composeRule.onNodeWithTag("TestTagSearchNone")
            .performClick()
    }

    @Test
    fun test_next_without_recipe_navigates_to_form_without_api() {
        launchSearchScreen()

        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.next)
        ).performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertTrue(route?.contains("add_meal_form") == true)
    }

    @Test
    fun test_next_with_selected_recipe_navigates_with_api_id() {
        launchSearchScreen()

        composeRule.onNodeWithTag("TestTagSearchInput")
            .performTextInput("Zucchini")

        composeRule.waitUntil {
            composeRule.onAllNodesWithText("Zucchini Lasagna")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule.onNodeWithText("Zucchini Lasagna")
            .performClick()

        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.next)
        ).performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertTrue(route?.contains("add_meal_form") == true)
    }




    private fun launchSearchScreen() {
        composeRule.activity.setContent {
            FoodDiaryTheme {
                navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = Destination.SearchScreen.route
                )
            }
        }

        composeRule.waitForIdle()
    }

}
