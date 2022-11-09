package com.master.sr.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColorPalette = darkColors(
    primary = Color(0xFFF9A825),
    primaryVariant = Color(0xFFF9A825),
    secondary = Color(0xFFF9A825),
    secondaryVariant = Color(0xFFF9A825),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),

    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),

    error = Color(0xFFFF0000),
    onError = Color(0xFFFFFFFF)
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = ColorPalette,
        content = content
    )
}