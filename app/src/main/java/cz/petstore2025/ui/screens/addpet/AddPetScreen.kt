package cz.petstore2025.ui.screens.addpet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.petstore2025.R
import cz.petstore2025.navigation.INavigationRouter
import cz.petstore2025.ui.elements.BaseScreen
import cz.petstore2025.ui.elements.DropdownMenuCategory
import cz.petstore2025.ui.elements.PhotoUrlPicker
import cz.petstore2025.ui.elements.TagInputField
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    navigation: INavigationRouter,
    viewModel: AddPetViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.success) {
        if (state.success) {
            viewModel.showTemporaryLoading()
            navigation.getNavController()
                .previousBackStackEntry
                ?.savedStateHandle
                ?.set("refreshList", true)
            delay(6000)
            navigation.returnBack()
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.add_pet),
        onBackClick = { navigation.returnBack() },
        showLoading = state.loading
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChanged,
                label = { Text(stringResource(R.string.pet_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            DropdownMenuCategory(
                selectedCategory = state.categorySelection,
                onCategorySelected = viewModel::onCategorySelectionChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            TagInputField(
                tags = state.tags,
                onTagsChange = viewModel::onTagsChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            PhotoUrlPicker(
                selectedUris = state.photoUris,
                onPhotoUrisChange = viewModel::onPhotoUrisChange
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.addPet() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }

    state.error?.let { errRes ->
        AlertDialog(
            onDismissRequest = viewModel::clearError,
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = {
                Text(
                    text = stringResource(R.string.add_failed_title),
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(text = stringResource(errRes))
            },
            confirmButton = {
                TextButton(onClick = viewModel::clearError) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }
}


