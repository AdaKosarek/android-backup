package cz.mendelu.pef.fooddiary.ui.elements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
){
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            color = OrangePrimary,
            modifier = Modifier.align(Alignment.Center)

        )
    }
}