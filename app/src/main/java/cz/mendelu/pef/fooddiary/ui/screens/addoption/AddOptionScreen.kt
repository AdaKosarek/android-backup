package cz.mendelu.pef.fooddiary.ui.screens.addoption

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.theme.BlueDark
import cz.mendelu.pef.fooddiary.ui.theme.BlueLightTile
import cz.mendelu.pef.fooddiary.ui.theme.BlueLightTileBorder
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangeDark
import cz.mendelu.pef.fooddiary.ui.theme.OrangeLightTile
import cz.mendelu.pef.fooddiary.ui.theme.OrangeLightTileBorder
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin
import cz.mendelu.pef.fooddiary.ui.theme.halfMargin

@Composable
fun AddOptionScreen(
    navigation: INavigationRouter,
) {
    BaseScreen(
        topBarText = stringResource(R.string.add_option_title),
        currentDestination = Destination.AddOptionScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        onBackClick = { navigation.returnBack() }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.add_option_subtitle),
                color = GrayText,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            AddOptionTile(
                backgroundColor = OrangeLightTile,
                borderColor = OrangeLightTileBorder,
                iconBackground = OrangeDark,
                icon = Icons.Outlined.PhotoCamera,
                title = stringResource(R.string.add_option_take_picture_title),
                subtitle = stringResource(R.string.add_option_take_picture_desc),
                onClick = {
                    // TODO: navigate to camera / gallery flow
                }
            )

            Spacer(modifier = Modifier.height(halfMargin()))

            AddOptionTile(
                backgroundColor = BlueLightTile,
                borderColor = BlueLightTileBorder,
                iconBackground = BlueDark,
                icon = Icons.Outlined.Search,
                title = stringResource(R.string.add_option_search_recipe_title),
                subtitle = stringResource(R.string.add_option_search_recipe_desc),
                onClick = {
                    navigation.navigateTo(Destination.SearchScreen)
                }
            )
        }
    }
}

@Composable
fun AddOptionTile(
    backgroundColor: Color,
    borderColor: Color,
    iconBackground: Color,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(iconBackground, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(basicMargin()))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(halfMargin()))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = GrayText
            )
        }
    }
}

