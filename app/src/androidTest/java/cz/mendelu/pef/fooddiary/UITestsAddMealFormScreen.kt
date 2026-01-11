package cz.mendelu.pef.fooddiary
import android.Manifest
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.NavGraph
import cz.mendelu.pef.fooddiary.ui.theme.FoodDiaryTheme
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.test.rule.GrantPermissionRule
import cz.mendelu.pef.fooddiary.fake.FakeSavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.ui.activities.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsAddMealFormScreen {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val locationPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(
            Manifest.permission.ACCESS_FINE_LOCATION
        )


    @Inject
    lateinit var fakeSavedRepository: FakeSavedMealsLocalRepository

    @Before
    fun setup() = runBlocking {
        hiltRule.inject()
        fakeSavedRepository.deleteAll()
    }


    @Test
    fun test_add_meal_form_is_displayed() {
        launchAddMealForm(apiId = null)

        composeRule.onNodeWithTag("TestTagCustomName")
            .assertIsDisplayed()

        composeRule.onNodeWithTag("TestTagSaveButton")
            .assertIsDisplayed()
    }


    @Test
    fun test_save_disabled_when_name_empty() {
        launchAddMealForm(apiId = null)

        composeRule.onNodeWithTag("TestTagSaveButton")
            .assertIsNotEnabled()
    }


    @Test
    fun test_save_meal_without_recipe_navigates_back() {
        launchAddMealForm(apiId = null)

        composeRule.onNodeWithTag("TestTagCustomName")
            .performTextInput("My Lunch")

        composeRule.onNodeWithTag("TestTagSaveButton")
            .performClick()

        composeRule.waitUntil {
            navController.currentBackStackEntry
                ?.destination
                ?.route == Destination.SavedScreen.route
        }
    }


    @Test
    fun test_save_meal_with_api_recipe_navigates_back() {
        launchAddMealForm(apiId = 101L)

        composeRule.waitUntil {
            composeRule.onAllNodesWithTag("TestTagCustomName")
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithTag("TestTagCustomName")
            .performTextInput("Zucchini Lunch")

        composeRule.onNodeWithTag("TestTagSaveButton")
            .performClick()

        composeRule.waitUntil {
            navController.currentBackStackEntry
                ?.destination
                ?.route == Destination.SavedScreen.route
        }
    }


    private fun launchAddMealForm(apiId: Long?) {
        composeRule.activity.setContent {
            FoodDiaryTheme {
                navController = rememberNavController()

                NavGraph(
                    navController = navController,
                    startDestination =
                        "${Destination.AddMealFormScreen.route}/${apiId ?: -1}"
                )
            }
        }
    }
}
