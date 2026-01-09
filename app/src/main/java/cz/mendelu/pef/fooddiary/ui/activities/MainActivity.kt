package cz.mendelu.pef.fooddiary.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.NavGraph
import cz.mendelu.pef.fooddiary.ui.theme.FoodDiaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodDiaryTheme {
                NavGraph(startDestination = Destination.SavedScreen.route)
            }
        }
    }
}
