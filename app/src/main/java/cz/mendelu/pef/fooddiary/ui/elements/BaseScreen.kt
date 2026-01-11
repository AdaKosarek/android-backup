@file:OptIn(ExperimentalMaterial3Api::class)
package cz.mendelu.pef.fooddiary.ui.elements

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.pef.fooddiary.R
import cz.mendelu.pef.fooddiary.navigation.Destination

const val TestTagBackButton = "TestTagBackButton"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    topBarText: String? = null,
    currentDestination: Destination,
    onBottomNavClick: (Destination) -> Unit,
    onBackClick: (() -> Unit)? = null,
    hideTopBar: Boolean = false,
    placeholderScreenContent: PlaceholderScreenContent? = null,
    showLoading: Boolean = false,
    floatingActionButton: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (paddingValues: PaddingValues) -> Unit) {

    Scaffold(
        floatingActionButton = floatingActionButton,
        topBar = {
            if (!hideTopBar) {
                TopAppBar(
                    title = {
                        if (topBarText != null) {
                            Text(
                                text = topBarText,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(start = 0.dp).testTag("TestTagTopBarTitle")
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                    actions = actions,
                    navigationIcon = {
                        if (onBackClick != null) {
                            IconButton(
                                modifier = Modifier.testTag(TestTagBackButton),
                                onClick = onBackClick
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back),
                                )
                            }
                        }
                    }
                )
            }
        },

        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                onItemClick = onBottomNavClick
            )
        }
    ) { paddingValues ->

        when {
            placeholderScreenContent != null -> {
                PlaceHolderScreen(content = placeholderScreenContent)
            }

            showLoading -> {
                LoadingScreen()
            }

            else -> {
                content(paddingValues)
            }
        }
    }
}