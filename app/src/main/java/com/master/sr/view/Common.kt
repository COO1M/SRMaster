package com.master.sr.view

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun Btn(
    @StringRes labelId: Int,
    enabled: Boolean = true,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(50),
        onClick = onClick
    ) {
        Text(
            text = stringResource(labelId),
            modifier = Modifier.padding(10.dp, 5.dp),
            color = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun ImgBtn(
    @DrawableRes iconId: Int,
    @StringRes labelId: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.size(50.dp),
        enabled = enabled,
        shape = RoundedCornerShape(50),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(labelId),
            tint = MaterialTheme.colors.onPrimary
        )
    }
}

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