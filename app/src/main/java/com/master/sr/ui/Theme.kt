package com.master.sr.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val ColorPalette = darkColors(
    primary = Color(0xFFF9A825),
    primaryVariant = Color(0xFFF9A825),
    secondary = Color(0xFFF9A825),
    secondaryVariant = Color(0xFFF9A825),
    onPrimary = Color.White,
    onSecondary = Color.White,

    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    onBackground = Color.White,
    onSurface = Color.White,

    error = Color(0xFFE53935),
    onError = Color.White
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    rememberSystemUiController().setSystemBarsColor(Color.Transparent, false)
    MaterialTheme(
        colors = ColorPalette,
        content = content
    )
}