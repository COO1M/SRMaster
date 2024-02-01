package com.master.sr.feature.menu

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.master.sr.R
import com.master.sr.theme.AppTheme
import com.master.sr.view.HeadText
import com.master.sr.view.HtmlText
import com.master.sr.view.ImgBtn

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuScreen(
    navigateBack: (Boolean) -> Unit
) {
    val vm: MenuViewModel = viewModel()
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    BackHandler {
        navigateBack(uiState.initialCompressImageIndex != uiState.compressImageIndex)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {

        ImgBtn(
            iconId = R.drawable.ic_baseline_close,
            labelId = R.string.back,
            modifier = Modifier.padding(start = 20.dp, top = 50.dp),
            onClick = { navigateBack(uiState.initialCompressImageIndex != uiState.compressImageIndex) }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                HeadText(id = R.string.setting)
            }
            item {
                MenuItem(
                    titleId = R.string.compress_image,
                    infoId = R.string.compress_image_info,
                    item = vm.compressImageItems[uiState.compressImageIndex],
                    items = vm.compressImageItems,
                    itemsClick = { vm.changeCompressImageIndex(it) }
                )
            }
            item {
                MenuItem(
                    titleId = R.string.model_backend,
                    infoId = R.string.model_backend_info,
                    item = vm.modelBackendItems[uiState.modelBackendIndex],
                    items = vm.modelBackendItems,
                    itemsClick = { vm.changeModelBackendIndex(it) }
                )
            }
            stickyHeader {
                HeadText(id = R.string.other)
            }
            item {
                HtmlText(
                    id = R.string.other_content,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}

@Composable
fun MenuItem(
    @StringRes titleId: Int,
    @StringRes infoId: Int,
    item: String,
    items: Array<String>,
    itemsClick: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = stringResource(titleId), style = MaterialTheme.typography.subtitle1)
            Text(text = stringResource(infoId), style = MaterialTheme.typography.caption)
        }
        Text(text = item, style = MaterialTheme.typography.body2)
        Box {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_unfold_more),
                contentDescription = stringResource(R.string.choose)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEachIndexed { i, s ->
                    DropdownMenuItem(
                        onClick = {
                            itemsClick(i)
                            expanded = false
                        }
                    ) {
                        Text(text = s, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MenuScreenPreview() {
    AppTheme {
        MenuScreen(
            navigateBack = {}
        )
    }
}