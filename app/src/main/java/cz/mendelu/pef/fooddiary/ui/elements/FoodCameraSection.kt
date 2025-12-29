package cz.mendelu.pef.fooddiary.ui.elements

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cz.mendelu.pef.fooddiary.analyzer.FoodImageLabelAnalyzer
import cz.mendelu.pef.fooddiary.ui.screens.search.SearchViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.ui.screens.search.SearchScreenUIState
import cz.mendelu.pef.fooddiary.ui.theme.GrayText

@Composable
fun FoodCameraSection(
    viewModel: SearchViewModel,
    state: SearchScreenUIState
) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    if (!hasPermission) {
        Text(text = stringResource(R.string.cam_permission))
        return
    }

    val labeler = remember {
        FoodImageLabelAnalyzer { label, confidence ->
            viewModel.onFoodDetected(label, confidence)
        }
    }

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            CameraComposeView(
                modifier = Modifier.fillMaxSize(),
                onImageCaptured = { image ->
                    labeler.process(image)
                }
            )
        }

        state.cameraMessage?.let { msg ->
            Text(
                text = stringResource(msg),
                color = GrayText,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

