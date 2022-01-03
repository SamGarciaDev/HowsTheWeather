package edu.samgarcia.howstheweather.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Blue200 = Color(0xFF90CAF9)
val Blue500 = Color(0xFF2196F3)
val Blue700 = Color(0xFF1976D2)
val Red200 = Color(0xFFEF9A9A)

val Colors.topAppBarBackground
    @Composable
    get() = if (isLight) primary else Color.Black