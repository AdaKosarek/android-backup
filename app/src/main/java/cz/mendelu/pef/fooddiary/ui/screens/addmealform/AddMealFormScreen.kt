package cz.mendelu.pef.fooddiary.ui.screens.addmealform
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.fooddiary.ui.theme.ChipBackground
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin
@Composable
fun AddMealFormScreen(
    navigation: INavigationRouter,
    apiId: Long?,
    viewModel: AddMealFormViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onPhotoSelected(it.toString()) }
    }

    LaunchedEffect(apiId) {
        viewModel.init(apiId)
    }

    LaunchedEffect(state.value.savedSuccessfully) {
        if (state.value.savedSuccessfully) {
            navigation.navigateTo(Destination.SavedScreen)
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.add_meal),
        currentDestination = Destination.AddMealFormScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        onBackClick = { navigation.returnBack() },
        showLoading = state.value.loading,
        placeholderScreenContent = state.value.error?.let {
            PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(it.messageRes)
            )
        }
    ) { padding ->
        AddMealFormContent(
            paddingValues = padding,
            state = state.value,
            onCustomNameChange = viewModel::onCustomNameChange,
            onNoteChange = viewModel::onNoteChange,
            onPlaceNameChange = viewModel::onPlaceNameChange,
            onPickImage = {
                imagePickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onSaveClick = {
                viewModel.saveMeal(
                    latitude = null,
                    longitude = null
                )
            }
        )
    }
}

@Composable
fun AddMealFormContent(
    paddingValues: PaddingValues,
    state: AddMealFormUIState,
    onCustomNameChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onPlaceNameChange: (String) -> Unit,
    onPickImage: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = basicMargin())
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(ChipBackground)
                .clickable { onPickImage() },
            contentAlignment = Alignment.Center
        ) {
            if (state.userPhotoUri != null) {
                AsyncImage(
                    model = state.userPhotoUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.CameraAlt,
                        contentDescription = null,
                        tint = GrayText,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.add_photo_optional),
                        color = GrayText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(basicMargin()))

        OutlinedTextField(
            value = state.customName,
            onValueChange = onCustomNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.custom_name)) },
            isError = state.customName.isBlank(),
            singleLine = true
        )

        if (state.customName.isBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.custom_name_required),
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.userNote,
            onValueChange = onNoteChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.note_optional)) },
            minLines = 3
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.placeName,
            onValueChange = onPlaceNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.place_optional)) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSaveClick,
            enabled = state.customName.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
        ) {
            Text(
                text = stringResource(R.string.save),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
