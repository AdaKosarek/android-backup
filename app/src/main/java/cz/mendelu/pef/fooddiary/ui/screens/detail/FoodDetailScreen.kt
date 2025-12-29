package cz.mendelu.pef.fooddiary.ui.screens.detail

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.elements.IngredientRow
import cz.mendelu.pef.fooddiary.ui.elements.InstructionSection
import cz.mendelu.pef.fooddiary.ui.elements.NutritionSection
import cz.mendelu.pef.fooddiary.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.fooddiary.ui.theme.ChipBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.halfMargin

@Composable
fun FoodDetailScreen(
    navigation: INavigationRouter,
    foodId: Long,
    viewModel: FoodDetailViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(foodId) {
        viewModel.loadRecipe(foodId)
    }

    BaseScreen(
        hideTopBar = true,
        currentDestination = Destination.FoodDetailScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        showLoading = state.value.loading,
        placeholderScreenContent =
            state.value.error?.let {
                PlaceholderScreenContent(
                    image = null,
                    title = null,
                    text = stringResource(id = it)
                )
            }
    ) { paddingValues ->

        FoodDetailScreenContent(
            paddingValues = paddingValues,
            recipe = state.value.recipe,
            loading = state.value.loading,
            onBackClick = { navigation.returnBack() }
        )
    }
}

@Composable
fun FoodDetailScreenContent(
    paddingValues: PaddingValues,
    recipe: RecipeDetail?,
    loading: Boolean,
    onBackClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize()) {}
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
            .verticalScroll(rememberScrollState())
            .testTag("TestTagDetailContainer")
    ) {

        if (recipe == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.no_detail_data),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            //foto
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
            ) {

                AsyncImage(
                    model = recipe.image ?: R.drawable.foods_common,
                    contentDescription = recipe.title,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                //back
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 36.dp)
                        .align(Alignment.TopStart)
                        .background(
                            Color.White,
                            CircleShape
                        )
                        .testTag("TestTagDetailBackButton")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GrayText
                    )
                }
                //srdce
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .padding(end = 12.dp, top = 36.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            Color.White,
                            shape = CircleShape
                        )
                        .testTag("TestTagDetailFavorite")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "favorite",
                        tint = if (isFavorite) OrangePrimary else GrayText
                    )
                }
            }


            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                //.padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = recipe.title ?: "Food",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.testTag("TestTagDetailTitle")
                )

                Spacer(modifier = Modifier.height(12.dp))

                //chips
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = GrayText)
                    Text("${recipe.readyInMinutes ?: "-"} min", color = GrayText)

                    Icon(Icons.Outlined.People, contentDescription = null, tint = GrayText)
                    Text("${recipe.servings ?: "-"} servings", color = GrayText)

                    recipe.dishTypes?.firstOrNull()?.let {
                        Box(
                            modifier = Modifier
                                .background(ChipBackground, RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = it.replaceFirstChar(Char::titlecase),
                                color = GrayText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                //nutritions
                Text(
                    text = stringResource(R.string.nutritional_values),
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.testTag("TestTagDetailNutrition")) {
                    NutritionSection(recipe)
                }

                Spacer(modifier = Modifier.height(24.dp))

                //ingredients
                Text(
                    text = stringResource(R.string.ingredients),
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(12.dp))


                Column(Modifier.testTag("TestTagDetailIngredientsList")) {
                    recipe.extendedIngredients?.forEach { ing ->
                        IngredientRow(ingredient = ing,
                            modifier = Modifier.testTag("TestTagDetailIngredient"))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                //instructions
                Text(
                    text = stringResource(R.string.preparation_instructions),
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.testTag("TestTagDetailInstruction")) {
                    InstructionSection(recipe)
                }

                Spacer(modifier = Modifier.height(32.dp))

                //save
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(halfMargin()))

                    Text(
                        text = stringResource(R.string.save_food),
                        color = Color.White
                    )
                }

            }
        }
    }
}

