package cz.mendelu.pef.fooddiary
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.NavGraph
import cz.mendelu.pef.fooddiary.ui.activities.MainActivity
import cz.mendelu.pef.fooddiary.ui.theme.FoodDiaryTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import cz.mendelu.pef.fooddiary.fake.FakeSavedMealsLocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.FixMethodOrder
import org.junit.runners.MethodSorters
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsSettingsScreen {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeRepository: FakeSavedMealsLocalRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_settings_screen_is_displayed() {
        launchSettingsScreen()

        composeRule
            .onNodeWithText(
                composeRule.activity.getString(R.string.data_manage)
            )
            .assertIsDisplayed()
    }

    @Test
    fun test_click_clear_data_opens_confirm_dialog() {
        launchSettingsScreen()

        composeRule
            .onNodeWithTag("TestTagClearAppDataCard")
            .performClick()

        composeRule
            .onNodeWithTag("TestTagConfirmDialog")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun test_cancel_dialog_closes_it() {
        launchSettingsScreen()

        composeRule
            .onNodeWithTag("TestTagClearAppDataCard")
            .performClick()

        composeRule
            .onNodeWithTag("TestTagCancelDeleteButton")
            .performClick()

        composeRule
            .onNodeWithTag("TestTagConfirmDialog")
            .assertDoesNotExist()
    }

    @Test
    fun test_confirm_delete_clears_data_ui_state() {
        launchSettingsScreen()

        composeRule
            .onNodeWithTag("TestTagClearAppDataCard")
            .performClick()

        composeRule
            .onNodeWithTag("TestTagConfirmDeleteButton")
            .performClick()

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule
                .onAllNodesWithTag("TestTagDataCleared")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        composeRule
            .onNodeWithTag("TestTagDataCleared")
            .assertIsDisplayed()
    }

    private fun launchSettingsScreen() {
        composeRule.activity.setContent {
            FoodDiaryTheme {
                navController = rememberNavController()

                NavGraph(
                    navController = navController,
                    startDestination = Destination.SettingsScreen.route
                )
            }
        }
    }
}
