package cz.mendelu.pef.fooddiary.ui.screens.settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.navigation.Destination
import cz.mendelu.pef.fooddiary.navigation.INavigationRouter
import cz.mendelu.pef.fooddiary.ui.elements.BaseScreen
import cz.mendelu.pef.fooddiary.ui.elements.PlaceholderScreenContent
import cz.mendelu.pef.fooddiary.ui.theme.GrayText
import cz.mendelu.pef.fooddiary.ui.theme.OrangePrimary
import cz.mendelu.pef.fooddiary.ui.theme.RedDark
import cz.mendelu.pef.fooddiary.ui.theme.RedLight
import cz.mendelu.pef.fooddiary.ui.theme.basicMargin
import cz.mendelu.pef.fooddiary.ui.theme.halfMargin

@Composable
fun SettingsScreen(
    navigation: INavigationRouter,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = stringResource(R.string.nav_settings),
        currentDestination = Destination.SettingsScreen,
        onBottomNavClick = { navigation.navigateTo(it) },
        showLoading = state.value.isClearingData,
        placeholderScreenContent = state.value.error?.let {
            PlaceholderScreenContent(
                image = null,
                title = null,
                text = stringResource(it)
            )
        }
    ) { paddingValues ->

        SettingsScreenContent(
            paddingValues = paddingValues,
            state = state.value,
            actions = viewModel
        )
    }
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    state: SettingsUIState,
    actions: SettingsActions
) {
    if (state.showConfirmDialog) {
        AlertDialog(
            modifier = Modifier.testTag("TestTagConfirmDialog"),
            onDismissRequest = actions::onDismissClearDialog,
            title = {
                Text(stringResource(R.string.remove_app_data))
            },
            text = {
                Text(stringResource(R.string.remove_app_data_confirm))
            },
            confirmButton = {
                TextButton(modifier = Modifier.testTag("TestTagConfirmDeleteButton"),
                    onClick = actions::onConfirmClearAppData) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = RedDark
                    )
                }
            },
            dismissButton = {
                TextButton(modifier = Modifier.testTag("TestTagCancelDeleteButton"),
                    onClick = actions::onDismissClearDialog) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    if (state.dataCleared) {
        Text(
            text = stringResource(R.string.data_cleared),
            modifier = Modifier.testTag("TestTagDataCleared")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.data_manage),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(halfMargin()))

        Card(
            modifier = Modifier.fillMaxWidth().testTag("TestTagClearAppDataCard")
                                .clickable { actions.onClearAppDataClick() },
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(basicMargin()),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(RedLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = RedDark
                    )
                }

                Spacer(modifier = Modifier.width(basicMargin()))

                Text(
                    text = stringResource(R.string.remove_app_data)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.about_app),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(halfMargin()))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(OrangePrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${stringResource(R.string.version)} ${state.appVersion}",
                    style = MaterialTheme.typography.bodySmall,
                    color = GrayText
                )
            }
        }
    }
}
