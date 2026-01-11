package cz.mendelu.pef.fooddiary.ui.screens.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
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
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedFilter = remember { mutableStateOf(SavedFilter.SAVED) }
    val meals = (state as? SavedScreenUIState.Success)?.meals.orEmpty()

    val filteredMeals = remember(meals, selectedFilter.value) {
        when (selectedFilter.value) {
            SavedFilter.SAVED ->
                meals.filter { it.source != SavedMealSource.API_ONLY }

            SavedFilter.FAVORITES ->
                meals.filter { it.isFavorite }

            SavedFilter.RECIPES ->
                meals.filter { it.source == SavedMealSource.API_ONLY }
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.nav_saved),
        currentDestination = Destination.SavedScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag("TestTagSavedFab"),
                onClick = { navigation.navigateTo(Destination.SearchScreen) },
                containerColor = OrangePrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .background(ScreenBackground)
        ) {

            SavedFilterSwitcher(
                selected = selectedFilter.value,
                onSelected = { selectedFilter.value = it }
            )

            if (filteredMeals.isEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(
                        when (selectedFilter.value) {
                            SavedFilter.SAVED -> R.string.saved_empty_text
                            SavedFilter.FAVORITES -> R.string.favorites_empty_text
                            SavedFilter.RECIPES -> R.string.recipes_empty_text
                        }
                    ),
                    color = GrayText,

                )
                Spacer(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier.testTag("TestTagSavedList")
                ) {
                    items(filteredMeals) { meal ->
                        SavedMealRow(
                            meal = meal,
                            onClick = {
                                navigation.navigateToSavedDetail(meal.localId)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SavedMealRow(
    meal: SavedMeal,
    onClick: () -> Unit
) {
    val isApiOnly = meal.source == SavedMealSource.API_ONLY

    val matrix = ColorMatrix().apply {
        setToSaturation(if (isApiOnly) 0f else 1f)
    }

    val imageModel =
        meal.userPhotoUri
            ?: meal.apiImage
            ?: R.drawable.foods_common

    Card(
        modifier = Modifier
            .testTag("TestTagSavedItem_${meal.localId}")
            .padding(horizontal = basicMargin(), vertical = halfMargin())
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ){
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = imageModel,
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

@Composable
fun SavedFilterSwitcher(
    selected: SavedFilter,
    onSelected: (SavedFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(basicMargin())
    ) {
        SavedFilter.entries.forEach { filter ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onSelected(filter) }
                    .testTag("TestTagFilter_${filter.name}")
            ) {
                Text(
                    text = when (filter) {
                        SavedFilter.SAVED -> stringResource(R.string.saved_filter)
                        SavedFilter.FAVORITES -> stringResource(R.string.favorites_filter)
                        SavedFilter.RECIPES -> stringResource(R.string.recipes_filter)
                    },
                    color = if (selected == filter) OrangePrimary else GrayText
                )

                Spacer(modifier = Modifier.height(6.dp))

                if (selected == filter) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(40.dp)
                            .background(OrangePrimary)
                    )
                }
            }
        }
    }
}

