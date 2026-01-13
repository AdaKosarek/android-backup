package cz.mendelu.pef.pokus1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.mendelu.pef.pokus1.navigation.Destination
import cz.mendelu.pef.pokus1.navigation.NavGraph
import cz.mendelu.pef.pokus1.ui.theme.Pokus1Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pokus1Theme {
                NavGraph(startDestination = Destination.ListScreen.route)
            }
        }
    }
}

