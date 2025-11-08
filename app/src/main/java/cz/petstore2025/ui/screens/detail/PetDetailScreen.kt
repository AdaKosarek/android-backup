package cz.petstore2025.ui.screens.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import cz.petstore2025.model.Pet
import cz.petstore2025.navigation.INavigationRouter
import cz.petstore2025.navigation.PetDetailDestination
import cz.petstore2025.ui.elements.BaseScreen
import cz.petstore2025.ui.elements.PlaceholderScreenContent
import cz.petstore2025.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PetDetailScreen(
    navigation: INavigationRouter,
    destination: PetDetailDestination,
    viewModel: PetDetailViewModel = hiltViewModel()
){

    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(destination) {
        viewModel.loadPetDetail(destination.petId)
    }


    LaunchedEffect(state.value.deletionSuccess) {
        if (state.value.deletionSuccess) {
            navigation.getNavController()
                .previousBackStackEntry
                ?.savedStateHandle
                ?.set("refreshList", true)

            delay(100)
            navigation.returnBack()
        }
    }

    LaunchedEffect(state.value.orderSuccess) {
        if (state.value.orderSuccess) {
            navigation.getNavController()
                .previousBackStackEntry
                ?.savedStateHandle
                ?.set("refreshList", true)
            delay(800)
            navigation.returnBack()
        }
    }

    BaseScreen(
        topBarText = "Detail",
        onBackClick = {
            coroutineScope.launch {
                navigation.getNavController()
                    ?.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("refreshList", true)

                delay(100)
                navigation.returnBack()
            }
        },
        showLoading = state.value.loading,
        placeholderScreenContent = when {//
            state.value.error != null -> PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(state.value.error!!)
            )
            !state.value.loading && state.value.pet == null -> PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(R.string.no_detail_data)
            )
            else -> null
        },
        actions = {
            IconButton(onClick = { viewModel.deletePet(destination.petId) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_pet)
                )
            }
        }
    ) {
        PetDetailScreenContent(
            paddingValues = it,
            pet = state.value.pet,
            onOrder = { quantity ->
                viewModel.orderPet(destination.petId, quantity)
            }
        )
    }

    state.value.deletionError?.let { errRes ->
        AlertDialog(
            onDismissRequest = {
                viewModel.clearDeletionError()
            },
            confirmButton = {
                TextButton(onClick = { viewModel.clearDeletionError() }) {
                    Text(stringResource(R.string.ok))
                }
            },
            title = {
                Text("Delete failed title")
            },
            text = {
                Text(stringResource(errRes))
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )
    }

    state.value.orderError?.let { errRes ->
        AlertDialog(
            onDismissRequest = { viewModel.clearOrderError() },
            confirmButton = {
                TextButton(onClick = { viewModel.clearOrderError() }) {
                    Text(stringResource(R.string.ok))
                }
            },
            title = { Text(stringResource(R.string.order_failed)) },
            text = { Text(stringResource(errRes)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        )
    }
}

@Composable
fun PetDetailScreenContent(
    paddingValues: PaddingValues,
    pet: Pet?,
    onOrder: (Int) -> Unit
) {
    val fallbackImage = painterResource(id = R.drawable.un_dog)
    var quantity by remember {
        mutableStateOf("1")
    }
    var showError by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (pet == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(R.string.no_detail_data),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            val name = pet.name?.takeIf { it.isNotBlank() } ?: "Pet"

            val category = pet.category?.name
                ?.takeIf { it.isNotBlank() && it.lowercase() != "string" }
                ?: "Unknown"

            val tagsText = pet.tags
                ?.mapNotNull { tag ->
                    val tagName = tag.name?.takeIf { it.isNotBlank() && it.lowercase() != "string" }
                    tagName ?: "Unknown"
                }
                ?.joinToString(", ")
                ?: "No tags"

            val originalStatus = pet.status?.lowercase() ?: "available"
            val normalizedStatus = when (originalStatus) {
                "available", "pending", "sold" -> originalStatus
                else -> "available"
            }

            val statusColor = when (normalizedStatus) {
                "available" -> Color(0xFF2E7D32)
                "pending" -> Color(0xFFF9A825)
                "sold" -> Color(0xFFC62828)
                else -> MaterialTheme.colorScheme.onSurfaceVariant
            }

            val photos = pet.photoUrls?.filter {
                it.isNotBlank() && it.lowercase() != "string" && !it.contains("example", ignoreCase = true) &&
                        (it.startsWith("http://") || it.startsWith("https://"))
            } ?: emptyList()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (photos.isNotEmpty()) {
                    val validPhotoUrl = photos.firstOrNull { url ->
                        url.isNotBlank() &&
                                url.lowercase() != "string" &&
                                (url.startsWith("http://") || url.startsWith("https://"))
                    }

                    val painter = if (validPhotoUrl != null) {
                        rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(validPhotoUrl)
                                .crossfade(true)
                                .error(R.drawable.un_dog)
                                .fallback(R.drawable.un_dog)
                                .build()
                        )
                    } else {
                        fallbackImage
                    }

                    Image(
                        painter = painter,
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                } else {
                    Image(
                        painter = fallbackImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Name: $name",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Category: $category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                            append("Status: ")
                        }
                        withStyle(style = SpanStyle(color = statusColor, fontWeight = FontWeight.SemiBold)) {
                            append(normalizedStatus)
                        }
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Tags: $tagsText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (photos.size > 1) {
                    Text(
                        text = "More photos:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(photos.drop(1)) { url ->
                            Image(
                                painter = rememberAsyncImagePainter(url),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                //ORDER
                Text(text = "Order pet", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = quantity,
                    onValueChange = {
                        quantity = it.filter { ch -> ch.isDigit() }
                        showError = false
                    },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (showError) {
                    Text(
                        text = "Enter a valid quantity",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val qty = quantity.toIntOrNull()
                        if (qty == null || qty <= 0) {
                            showError = true
                        } else {
                            onOrder(qty)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Order", style = MaterialTheme.typography.titleMedium)
                }

            }
        }
    }
}

