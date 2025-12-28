package cz.mendelu.pef.fooddiary.ui.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.database.SavedMeal
import cz.mendelu.pef.fooddiary.model.SavedMealSource
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.elements.LocationTag
import cz.mendelu.pef.fooddiary.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.fooddiary.ui.theme.CardBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.ScreenBackground
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin
import cz.mendelu.pef.fooddiary.ui.theme.halfMargin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SavedScreen(
    navigation: INavigationRouter,
    viewModel: SavedViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val meals = remember { mutableStateListOf<SavedMeal>() }

    when (val current = state.value) {
        is SavedScreenUIState.Default -> {
            viewModel.loadMeals()
        }
        is SavedScreenUIState.Success -> {
            meals.clear()
            meals.addAll(current.meals)
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.nav_saved),
        currentDestination = Destination.SavedScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigation.navigateTo(Destination.AddOptionScreen)
                },
                containerColor = OrangePrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        },
        placeholderScreenContent = if (meals.isEmpty()) {
            PlaceholderScreenContent(
                image = null,
                title = stringResource(R.string.saved_empty_title),
                text = stringResource(R.string.saved_empty_text)
            )
        } else null
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(ScreenBackground)
        ) {
            items(meals) { meal ->
                SavedMealRow(meal = meal)
            }
        }
    }
}


@Composable
fun SavedMealRow(
    meal: SavedMeal
) {
    val isApiOnly = meal.source == SavedMealSource.API_ONLY
    val matrix = ColorMatrix().apply {
        setToSaturation(if (isApiOnly) 0f else 1f)
    }


    Card(
        modifier = Modifier
            .padding(horizontal = basicMargin(), vertical = halfMargin())
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            //image
            AsyncImage(
                model = meal.userPhotoUri,
                contentDescription = meal.title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colorFilter = ColorFilter.colorMatrix(matrix),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(basicMargin()))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text = meal.customName ?: meal.title.orEmpty(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = SimpleDateFormat(
                        "dd. MM. yyyy",
                        Locale.getDefault()
                    ).format(Date(meal.savedTimestamp)),
                    color = GrayText,
                    style = MaterialTheme.typography.bodySmall
                )

                if (meal.hasLocation) {
                    Spacer(modifier = Modifier.height(6.dp))
                    LocationTag()
                }
            }
        }
    }
}
