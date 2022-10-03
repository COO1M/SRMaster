package com.master.sr

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

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
    enabled: Boolean = true,
    modifier: Modifier,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BaseBtn(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick,
            enabled = enabled,
            role = Role.Button,
            interactionSource = interactionSource,
            indication = null
        ),
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = MaterialTheme.typography.button
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = ButtonDefaults.MinWidth,
                            minHeight = ButtonDefaults.MinHeight
                        )
                        .indication(interactionSource, rememberRipple())
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}