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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.model.RecipeDetail
import cz.mendelu.pef.fooddiary.navigation.FoodDetailDestination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.elements.IngredientRow
import cz.mendelu.pef.fooddiary.ui.elements.InstructionSection
import cz.mendelu.pef.fooddiary.ui.elements.NutritionSection
import cz.mendelu.pef.fooddiary.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.fooddiary.ui.theme.ChipBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin

@Composable
fun FoodDetailScreen(
    navigation: INavigationRouter,
    destination: FoodDetailDestination,
    viewModel: FoodDetailViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(destination) {
        viewModel.loadRecipe(destination.foodId)
    }

    BaseScreen(
        topBarText = null,
        onBackClick = { navigation.returnBack() },
        showLoading = state.value.loading,
        placeholderScreenContent =
            state.value.error?.let {
                PlaceholderScreenContent(
                    image = null,
                    title = null,
                    text = stringResource(id = it)
                )
            }
    ) { padding ->

        FoodDetailScreenContent(
            paddingValues = padding,
            recipe = state.value.recipe
        )
    }
}

@Composable
fun FoodDetailScreenContent(
    paddingValues: PaddingValues,
    recipe: RecipeDetail?
) {
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
    ) {

        if (recipe == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.no_detail_data),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else{
            //foto
            Box(modifier = Modifier.fillMaxWidth()) {

                AsyncImage(
                    model = recipe.image ?: R.drawable.foods_common,
                    contentDescription = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            Color.White,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "favorite",
                        tint = if (isFavorite) OrangePrimary else GrayText
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = basicMargin())) {
                Text(
                    text = recipe.title ?: "",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            //chips
            Row(
                modifier = Modifier.padding(horizontal = basicMargin()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = GrayText)
                Text("${recipe.readyInMinutes ?: "-"} min", color = GrayText)

                Icon(Icons.Outlined.Person, contentDescription = null, tint = GrayText)
                Text("${recipe.servings ?: "-"} servings", color = GrayText)

                recipe.dishTypes?.firstOrNull()?.let {
                    Box(
                        modifier = Modifier
                            .background(ChipBackground, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = it.replaceFirstChar(Char::titlecase),
                            color = GrayText,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            //nutrition
            Text(
                text = stringResource(R.string.nutritional_values),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = basicMargin())
            )
            Spacer(modifier = Modifier.height(12.dp))

            NutritionSection(recipe)

            Spacer(modifier = Modifier.height(24.dp))

            //ingredients
            Text(
                text = stringResource(R.string.ingredients),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = basicMargin())
            )

            Spacer(modifier = Modifier.height(12.dp))

            recipe.extendedIngredients?.forEach { ing ->
                IngredientRow(ingredient = ing)
            }

            Spacer(modifier = Modifier.height(24.dp))

            //instructions
            Text(
                text = stringResource(R.string.preparation_instructions),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = basicMargin())
            )

            Spacer(modifier = Modifier.height(12.dp))

            InstructionSection(recipe)

            Spacer(modifier = Modifier.height(32.dp))

            //save
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = basicMargin())
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
            ) {
                Text(stringResource(R.string.save_food), color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

    }
}

