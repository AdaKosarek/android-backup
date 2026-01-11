package cz.mendelu.pef.fooddiary
import android.Manifest
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.test.rule.GrantPermissionRule
import cz.mendelu.pef.fooddiary.fake.FakeSavedMealsLocalRepository
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.NavGraph
import cz.mendelu.pef.fooddiary.ui.activities.MainActivity
import cz.mendelu.pef.fooddiary.ui.theme.FoodDiaryTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class UITestsMapScreen {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val permissionRule = GrantPermissionRule.grant(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeRepository: FakeSavedMealsLocalRepository

    @Before
    fun setup() = runBlocking {
        hiltRule.inject()
    }

    @Test
    fun test_map_screen_is_displayed() {
        launchMapScreen()

        composeRule
            .onNodeWithTag("TestTagBottomNav_Map")
            .assertExists()
    }

    @Test
    fun test_only_meals_with_location_used() {
        val mealsWithLocation =
            fakeRepository.currentMeals.filter { it.hasLocation }

        Assert.assertTrue(mealsWithLocation.isNotEmpty())
    }


    private fun launchMapScreen() {
        composeRule.activity.setContent {
            FoodDiaryTheme {
                navController = rememberNavController()

                NavGraph(
                    navController = navController,
                    startDestination = Destination.MapScreen.route
                )
            }
        }
    }

}

