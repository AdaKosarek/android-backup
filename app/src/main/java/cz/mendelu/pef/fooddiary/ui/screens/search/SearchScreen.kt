package cz.mendelu.pef.fooddiary.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.model.DiscoverRecipeItem
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.elements.FoodCameraSection
import cz.mendelu.pef.fooddiary.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.fooddiary.ui.theme.BlueLightTile
import cz.mendelu.pef.fooddiary.ui.theme.CardBackground
import cz.mendelu.pef.fooddiary.ui.theme.ChipBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin
import cz.mendelu.pef.fooddiary.ui.theme.halfMargin
import cz.mendelu.pef.fooddiary.utils.TestMode

@Composable
fun SearchScreen(
    navigation: INavigationRouter,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = stringResource(R.string.search_food),
        currentDestination = Destination.SavedScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        onBackClick = { navigation.returnBack() },
        placeholderScreenContent = state.value.error?.let {
            PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(it.communicationError)
            )
        }
    ) { padding ->

        SearchScreenContent(
            paddingValues = padding,
            state = state.value,
            onQueryChange = viewModel::onQueryChange,
            onSelectRecipe = viewModel::selectRecipe,
            onSelectNone = viewModel::selectNone,
            navigation = navigation,
            viewModel = viewModel
        )
    }
}

@Composable
fun SearchScreenContent(
    paddingValues: PaddingValues,
    state: SearchScreenUIState,
    onQueryChange: (String) -> Unit,
    onSelectRecipe: (DiscoverRecipeItem) -> Unit,
    onSelectNone: () -> Unit,
    navigation: INavigationRouter,
    viewModel: SearchViewModel
) {
    val isTest = TestMode.isRunningTest

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = basicMargin()),
        verticalArrangement = Arrangement.spacedBy(halfMargin())
    ) {

        if (!isTest) {
            item {
                FoodCameraSection(
                    viewModel = viewModel,
                    state = state
                )
            }
        }


        item {
            Text(text = stringResource(R.string.suggestions))
        }

        item {
            OutlinedTextField(
                value = state.query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth().testTag("TestTagSearchInput"),
                placeholder = { Text(stringResource(R.string.search_food_hint)) },
                leadingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp)
            )
        }

        item {
            SearchNoneRow(
                selected = state.selectedRecipe == null,
                onClick = onSelectNone
            )
        }

        when {
            state.loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = basicMargin()),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = OrangePrimary)
                    }
                }
            }

            state.recipes != null -> {
                items(state.recipes) { recipe ->
                    SearchRecipeRow(
                        recipe = recipe,
                        selected = state.selectedRecipe?.id == recipe.id,
                        onClick = { onSelectRecipe(recipe) }
                    )
                }
            }

            state.error != null -> {
                item {
                    Text(
                        text = stringResource(state.error.communicationError),
                        color = GrayText,
                        modifier = Modifier.padding(vertical = basicMargin())
                    )
                }
            }
        }

        item {
            Button(
                onClick = {
                    navigation.navigateToAddMealForm(
                        state.selectedRecipe?.id
                    )
                },
                enabled = state.recipes != null || state.selectedRecipe == null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("TestTagSearchNext"),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
            ) {
                Text(stringResource(R.string.next), color = Color.White)
            }
        }

        item {
            Spacer(modifier = Modifier.height(basicMargin()))
        }
    }
}


@Composable
fun SearchRecipeRow(
    recipe: DiscoverRecipeItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background =
        if (selected) BlueLightTile else CardBackground

    Card(
        modifier = Modifier
            .testTag("TestTagSearchRecipe_${recipe.id}")
            .padding(vertical = halfMargin())
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = recipe.image ?: R.drawable.foods_common,
                contentDescription = recipe.title,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(basicMargin()))

            Text(
                text = recipe.title ?: "Food",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun SearchNoneRow(
    selected: Boolean,
    onClick: () -> Unit
) {
    val background =
        if (selected) BlueLightTile else CardBackground

    Card(
        modifier = Modifier
            .padding(vertical = halfMargin())
            .testTag("TestTagSearchNone")
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ChipBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Block,
                    contentDescription = null,
                    tint = GrayText
                )
            }

            Spacer(modifier = Modifier.width(basicMargin()))

            Text(
                text = stringResource(R.string.search_none)
            )

        }
    }
}
