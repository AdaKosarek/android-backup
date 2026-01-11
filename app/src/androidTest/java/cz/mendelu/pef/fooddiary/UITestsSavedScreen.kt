package cz.mendelu.pef.fooddiary
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.fooddiary.fake.FakeSavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.ui.activities.MainActivity
import cz.mendelu.pef.fooddiary.ui.theme.FoodDiaryTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import javax.inject.Inject
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.NavGraph
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking


@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsSavedScreen {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeRepository: FakeSavedMealsLocalRepository

    @Before
    fun setup() = runBlocking {
        hiltRule.inject()
    }



    @Test
    fun test_switch_to_favorites_filter_shows_favorite() {
        launchSavedScreen()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("TestTagFilter_FAVORITES")
            .performClick()

        composeRule.onNodeWithText("Favorite Meal")
            .assertIsDisplayed()
    }



    @Test
    fun test_saved_meal_is_displayed() {
        launchSavedScreen()

        composeRule.waitForIdle()

        composeRule.onNodeWithTag("TestTagSavedItem_1")
            .assertIsDisplayed()
    }


    @Test
    fun test_click_on_saved_meal_navigates_to_detail() {
        launchSavedScreen()
        composeRule.waitForIdle()

        composeRule.onNodeWithTag("TestTagSavedItem_1")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        Thread.sleep(200)
        assertTrue(route?.contains("saved_detail") == true)
    }


    @Test
    fun test_fab_navigates_to_search() {
        launchSavedScreen()

        composeRule.onNodeWithTag("TestTagSavedFab")
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(Destination.SearchScreen.route, route)
    }

    private fun launchSavedScreen() {
        composeRule.activity.setContent {
            FoodDiaryTheme {
                navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = Destination.SavedScreen.route
                )
            }
        }
    }
}



