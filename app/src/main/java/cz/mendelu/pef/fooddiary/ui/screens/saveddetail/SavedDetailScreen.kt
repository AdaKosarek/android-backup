package cz.mendelu.pef.fooddiary.ui.screens.saveddetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
import cz.mendelu.pef.fooddiary.ui.elements.detail.IngredientRow
import cz.mendelu.pef.fooddiary.ui.elements.detail.NutritionSectionSavedMeal
import cz.mendelu.pef.fooddiary.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.fooddiary.ui.elements.detail.InstructionSectionSavedMeal
import cz.mendelu.pef.fooddiary.ui.elements.detail.SavedMealLocalInfoSection
import cz.mendelu.pef.fooddiary.ui.theme.ChipBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin

@Composable
fun SavedDetailScreen(
    navigation: INavigationRouter,
    localId: Long,
    viewModel: SavedDetailViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(localId) {
        viewModel.loadMeal(localId)
    }
    LaunchedEffect(state.value.deletedSuccessfully) {
        if (state.value.deletedSuccessfully) {
            navigation.returnBack()
        }
    }

    BaseScreen(
        hideTopBar = true,
        currentDestination = Destination.SavedDetailScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        showLoading = state.value.loading,
        placeholderScreenContent =
            state.value.error?.let {
                PlaceholderScreenContent(
                    image = null,
                    title = null,
                    text = stringResource(it)
                )
            }
    ) { paddingValues ->
        SavedDetailScreenContent(
            paddingValues = paddingValues,
            meal = state.value.meal,
            loading = state.value.loading,
            onBackClick = { navigation.returnBack() },
            isEditing = state.value.isEditing,
            actions = viewModel,
            state = state.value
        )
    }
}

@Composable
fun SavedDetailScreenContent(
    paddingValues: PaddingValues,
    meal: SavedMeal?,
    loading: Boolean,
    onBackClick: () -> Unit,
    isEditing: Boolean,
    actions: SavedDetailActions,
    state: SavedDetailScreenUIState
) {

    if (loading) {
        Box(modifier = Modifier.fillMaxSize())
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
            .verticalScroll(rememberScrollState())
    ) {

        if (meal == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.no_detail_data),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            return
        }

        val showLocalSection = meal.source == SavedMealSource.FAB || meal.source == SavedMealSource.API_PLUS_FAB
        val showApiSection = meal.source == SavedMealSource.API_ONLY || meal.source == SavedMealSource.API_PLUS_FAB

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {

            AsyncImage(
                model = meal.userPhotoUri ?: meal.apiImage ?: R.drawable.foods_common,
                contentDescription = meal.title,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(start = 12.dp, top = 36.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = GrayText
                )
            }
            if(showLocalSection) {
                IconButton(
                    onClick = actions::onToggleFavorite,
                    modifier = Modifier
                        .padding(end = 12.dp, top = 36.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector =
                            if (meal.isFavorite)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                        contentDescription = "favorite",
                        tint =
                            if (meal.isFavorite)
                                OrangePrimary
                            else
                                GrayText
                    )
                }
            }
        }

        //UZIVATEL
        if (showLocalSection) {
            SavedMealLocalInfoSection(
                meal = meal,
                actions = actions,
                isEditing = isEditing,
                state = state
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        //API
        if (showApiSection) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(basicMargin()))

                Text(
                    text = (meal.title + stringResource(R.string.recipe)),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = GrayText)
                    Text("${meal.readyInMinutes ?: "-"} min", color = GrayText)

                    Icon(Icons.Outlined.People, contentDescription = null, tint = GrayText)
                    Text("${meal.servings ?: "-"} servings", color = GrayText)

                    meal.dishTypes?.firstOrNull()?.let {
                        Box(
                            modifier = Modifier
                                .background(ChipBackground, RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(it.replaceFirstChar(Char::titlecase), color = GrayText)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.nutritional_values),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                meal.nutrition?.let {
                    NutritionSectionSavedMeal(it)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.ingredients),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                meal.extendedIngredients?.forEach { ing ->
                    IngredientRow(ingredient = ing)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.preparation_instructions),
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(12.dp))

                InstructionSectionSavedMeal(meal)

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

