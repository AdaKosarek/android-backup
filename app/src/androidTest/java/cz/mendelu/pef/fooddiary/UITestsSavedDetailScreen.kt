package cz.mendelu.pef.fooddiary
import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import cz.mendelu.pef.fooddiary.mock.SavedServerMock
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.NavGraph
import cz.mendelu.pef.fooddiary.ui.activities.MainActivity
import cz.mendelu.pef.fooddiary.ui.theme.FoodDiaryTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestsSavedDetailScreen {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_01_local_meal_shows_local_section() {
        launchDetail(SavedServerMock.savedFab.localId)

        composeRule
            .onNodeWithTag("TestTagLocalSection")
            .assertIsDisplayed()
    }

    @Test
    fun test_02_api_only_meal_hides_local_section() {
        launchDetail(SavedServerMock.savedApi.localId)

        composeRule
            .onNodeWithTag("TestTagLocalSection")
            .assertDoesNotExist()
    }

    @Test
    fun test_03_toggle_favorite_changes_state() {
        launchDetail(SavedServerMock.savedFab.localId)

        composeRule
            .onNodeWithTag("TestTagFavoriteButton")
            .performClick()

        composeRule.waitForIdle()

        composeRule
            .onNodeWithContentDescription("favorite")
            .assertExists()
    }

    @Test
    fun test_no_meal_shows_error_placeholder() {
        launchDetail(localId = 9999L)

        composeRule
            .onNodeWithTag("TestTagErrorPlaceholder")
            .assertIsDisplayed()
    }


    private fun launchDetail(localId: Long) {
        composeRule.activity.setContent {
            FoodDiaryTheme {
                NavGraph(
                    startDestination = "${Destination.SavedDetailScreen.route}/$localId"
                )
            }
        }
    }
}
