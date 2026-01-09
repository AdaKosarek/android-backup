package cz.mendelu.pef.fooddiary.ui.elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.ui.theme.CardBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary

@Composable
fun BottomNavigationBar(
    currentDestination: Destination,
    onItemClick: (Destination) -> Unit
) {
    val navItems = listOf(
        Destination.DiscoverScreen,
        Destination.SavedScreen,
        Destination.MapScreen,
        Destination.SettingsScreen
    )

    Column {
        // horní oddělovací linka
        HorizontalDivider(
            color =  Color(0xFFE0E0E0),
            thickness = 1.dp
        )

        NavigationBar(
            containerColor = CardBackground,
            tonalElevation = 0.dp
        ) {
            navItems.forEach { destination ->

                val selected = when (destination) {
                    Destination.DiscoverScreen ->
                        currentDestination == Destination.DiscoverScreen || currentDestination == Destination.FoodDetailScreen

                    Destination.SavedScreen ->
                        currentDestination == Destination.SavedScreen || currentDestination == Destination.SavedDetailScreen || currentDestination == Destination.SearchScreen || currentDestination == Destination.AddMealFormScreen

                    Destination.MapScreen ->
                        currentDestination == Destination.MapScreen

                    Destination.SettingsScreen ->
                        currentDestination == Destination.SettingsScreen

                    else -> destination.route == currentDestination.route
                }

                val labelRes = when (destination) {
                    Destination.DiscoverScreen -> R.string.nav_discover
                    Destination.SavedScreen -> R.string.nav_saved
                    Destination.MapScreen -> R.string.nav_map
                    Destination.SettingsScreen -> R.string.nav_settings
                    else -> null
                }

                NavigationBarItem(
                    selected = selected,
                    onClick = { onItemClick(destination) },
                    icon = {
                        Icon(
                            imageVector = when (destination) {
                                Destination.DiscoverScreen -> Icons.Outlined.Home
                                Destination.SavedScreen -> Icons.Outlined.BookmarkBorder
                                Destination.MapScreen -> Icons.Outlined.LocationOn
                                Destination.SettingsScreen -> Icons.Outlined.Settings
                                else -> Icons.Default.Info
                            },
                            contentDescription = destination.route,
                            modifier = Modifier.size(28.dp)
                        )
                    },
                    label = {
                        labelRes?.let {
                            Text(
                                text = stringResource(id = it),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OrangePrimary,
                        selectedTextColor = OrangePrimary,
                        unselectedIconColor = GrayText ,
                        unselectedTextColor = GrayText ,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}
