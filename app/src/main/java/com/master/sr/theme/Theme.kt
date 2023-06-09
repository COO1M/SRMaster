package com.master.sr.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = AppColor,
        typography = AppTypography,
        shapes = AppShape,
        content = content
    )
}