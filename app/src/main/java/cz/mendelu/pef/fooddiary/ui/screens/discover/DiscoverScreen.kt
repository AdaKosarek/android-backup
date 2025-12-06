package cz.mendelu.pef.fooddiary.ui.screens.discover

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.model.DiscoverRecipeItem
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.fooddiary.ui.theme.CardBackground
import cz.mendelu.pef.fooddiary.ui.theme.ChipBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangeLight
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.OrangeText
import cz.mendelu.pef.fooddiary.ui.theme.ScreenBackground
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin
import cz.mendelu.pef.fooddiary.ui.theme.halfMargin

@Composable
fun DiscoverScreen(
    navigation: INavigationRouter,
    viewModel: DiscoverViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    var selectedCategory by remember { mutableStateOf("All") }

    val categoryToApi = mapOf(
        "All" to null,
        "Main dish" to "main dish",
        "Soup" to "soup",
        "Desert" to "dessert",
        "Side dish" to "side dish"
    )

    //kdyz se zmeni type jidla
    LaunchedEffect(selectedCategory) {
        viewModel.loadRecipes(categoryToApi[selectedCategory])
    }

    BaseScreen(
        topBarText = "Discover",
        showLoading = false,
        placeholderScreenContent = if (state.value.error != null) {
            PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(state.value.error!!.communicationError)
            )
        } else null,
        floatingActionButton = {}
    ) { padding ->

        DiscoverScreenContent(
            paddingValues = padding,
            navigation = navigation,
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            recipes = state.value.recipes,
            loading = state.value.loading
        )
    }
}

@Composable
fun DiscoverScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    recipes: List<DiscoverRecipeItem>? = null,
    loading: Boolean
) {
    val categoryNames = listOf("All", "Main Dish", "Soup", "Desert", "Side dish")

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.background(ScreenBackground)
            .padding(paddingValues)
    ) {

        //kategorie
        LazyRow(
            modifier = Modifier.padding(horizontal = basicMargin(), vertical = halfMargin()),
            horizontalArrangement = Arrangement.spacedBy(halfMargin())
        ) {
            items(categoryNames) { category ->
                val isSelected = category == selectedCategory

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) OrangePrimary else ChipBackground)
                        .clickable { onCategorySelected(category) }
                        .padding(horizontal = 20.dp,
                            vertical = 10.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else GrayText
                    )
                }
            }
        }

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = OrangePrimary)
            }
        } else {
            recipes?.let { list ->
                LazyColumn {
                    items(list) { recipe ->
                        RecipeRow(
                            recipe = recipe,
                            selectedCategory = selectedCategory,
                            onClick = { navigation.navigateToFoodDetail(recipe.id!!) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RecipeRow(
    recipe: DiscoverRecipeItem,
    selectedCategory: String,
    onClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(false) }
    val servings = recipe.servings
    val dishType = if (selectedCategory != "All") selectedCategory else recipe.dishTypes?.firstOrNull()?.replaceFirstChar(Char::titlecase) ?: "Food"
    val time = recipe.readyInMinutes ?: "Unkn"
    val imageUrl = recipe.image ?: R.drawable.foods_common

    Card(
        modifier = Modifier
            .padding(horizontal = basicMargin(), vertical = halfMargin())
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {

            Box(modifier = Modifier.fillMaxWidth()) {

                AsyncImage(
                    model = imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
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

            Column(modifier = Modifier.padding(basicMargin())) {

                Text(
                    text = recipe.title ?: "Without name",
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    //typ jidla
                    Box(
                        modifier = Modifier
                            .background(ChipBackground, RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = dishType,
                            color = GrayText,
                            fontSize = 13.sp
                        )
                    }

                    //cas
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = GrayText,
                            modifier = Modifier.size(basicMargin())
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$time min",
                            color = GrayText,
                            fontSize = 13.sp
                        )
                    }

                    //pocet porci
                    Spacer(modifier = Modifier.weight(1f))
                    if (servings != null) {
                        Box(
                            modifier = Modifier
                                .background(OrangeLight, RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    tint = OrangeText,
                                    modifier = Modifier.size(basicMargin())
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$servings",
                                    color = OrangeText,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
