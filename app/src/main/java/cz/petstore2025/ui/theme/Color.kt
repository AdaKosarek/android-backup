package cz.petstore2025.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val primaryColor = Color(0xFFB657FF)
val secondaryColor = Color(0xFFFFE74A)
val tertiaryColor = Color(0xFFC77EFF)


@Composable
fun primaryColor() = primaryColor

@Composable
fun secondaryColor() = secondaryColor

@Composable
fun textPrimaryColor() = if (isSystemInDarkTheme()) Color.White else Color.Black

@Composable
fun textSecondaryColor() = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray

