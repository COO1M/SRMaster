package com.master.sr.view

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(
    @StringRes id: Int,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            TextView(ctx).apply {
                text = HtmlCompat.fromHtml(ctx.getString(id), HtmlCompat.FROM_HTML_MODE_LEGACY)
                isClickable = true
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        modifier = modifier
    )
}

@Composable
fun HeadText(
    @StringRes id: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id),
        style = MaterialTheme.typography.button,
        color = MaterialTheme.colors.primary,
        modifier = modifier.padding(
            start = 20.dp,
            end = 20.dp,
            top = 20.dp,
            bottom = 5.dp
        )
    )
}