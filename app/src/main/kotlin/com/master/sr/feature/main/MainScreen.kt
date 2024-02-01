package com.master.sr.feature.main

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.master.sr.R
import com.master.sr.app.App
import com.master.sr.theme.AppTheme
import com.master.sr.util.ToastUtil
import com.master.sr.view.Btn
import com.master.sr.view.HtmlText
import com.master.sr.view.ImgBtn
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    needClear: Boolean,
    navigateToMenu: () -> Unit
) {
    val vm: MainViewModel = viewModel()
    val uiState by vm.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()

    val selectPicture = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            vm.select(uri)
        } else {
            ToastUtil.short(R.string.no_select)
        }
    }

    LaunchedEffect(Unit) {
        if (needClear) vm.clear()
        vm.initSetting()
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(30.dp),
        sheetContent = {
            Divider(
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .size(30.dp, 4.dp)
                    .clip(RoundedCornerShape(50))
                    .align(Alignment.CenterHorizontally)
            )
            HtmlText(
                id = R.string.info_content,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, bottom = 30.dp, top = 10.dp)
                    .navigationBarsPadding()
            )
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            val (progress, btn_menu, btn_info, btn_compare, btn_select, btn_run, btn_save) = createRefs()
            createHorizontalChain(btn_select, btn_run, btn_save, chainStyle = ChainStyle.Packed)

            //Preview Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(if (uiState.comparing) uiState.startBmp else uiState.endBmp)
                    .build(),
                contentDescription = stringResource(R.string.preview),
                error = painterResource(R.drawable.ic_baseline_crop_free),
                modifier = Modifier.fillMaxSize()
            )

            //Loading Progress
            if (uiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(progress) {
                        centerTo(parent)
                    }
                )
            }

            //Menu ImageButton
            ImgBtn(
                iconId = R.drawable.ic_baseline_menu,
                labelId = R.string.menu,
                modifier = Modifier.constrainAs(btn_menu) {
                    top.linkTo(parent.top, 50.dp)
                    start.linkTo(parent.start, 20.dp)
                },
                onClick = { navigateToMenu() }
            )

            //Info ImageButton
            ImgBtn(
                iconId = R.drawable.ic_baseline_contact_support,
                labelId = R.string.info,
                modifier = Modifier
                    .constrainAs(btn_info) {
                        top.linkTo(parent.top, 50.dp)
                        end.linkTo(parent.end, 20.dp)
                    },
                onClick = { scope.launch { sheetState.show() } }
            )

            //Compare ImageButton
            ImgBtn(
                iconId = R.drawable.ic_baseline_flip,
                labelId = R.string.compare,
                enabled = !uiState.loading && uiState.startBmp != null && uiState.endBmp != null,
                modifier = Modifier
                    .constrainAs(btn_compare) {
                        top.linkTo(btn_info.bottom, 20.dp)
                        start.linkTo(btn_info.start)
                    }
                    .rotate(if (!uiState.comparing) 180f else 0f),
                onClick = { vm.compare() }
            )

            //Select Button
            Btn(
                labelId = R.string.select,
                enabled = !uiState.loading,
                modifier = Modifier.constrainAs(btn_select) {
                    bottom.linkTo(parent.bottom, 30.dp)
                },
                onClick = { selectPicture.launch("image/*") }
            )

            //Run Button
            Btn(
                labelId = R.string.run,
                enabled = !uiState.loading && uiState.startBmp != null,
                modifier = Modifier
                    .constrainAs(btn_run) {
                        top.linkTo(btn_select.top)
                    }
                    .padding(horizontal = 20.dp),
                onClick = { vm.run() }
            )

            //Save Button
            Btn(
                labelId = R.string.save,
                enabled = !uiState.loading && uiState.startBmp != null && uiState.endBmp != null,
                modifier = Modifier.constrainAs(btn_save) {
                    top.linkTo(btn_select.top)
                },
                onClick = { vm.save() }
            )

        }
    }

    var lastBackMillis by remember { mutableStateOf<Long>(0) }
    BackHandler {
        if (sheetState.currentValue != ModalBottomSheetValue.Hidden) {
            scope.launch { sheetState.hide() }
        } else {
            val thisBackMillis = System.currentTimeMillis()
            if (thisBackMillis - lastBackMillis < 2000) {
                App.act.finish()
                exitProcess(0)
            } else {
                lastBackMillis = thisBackMillis
                ToastUtil.short(R.string.press_again_to_exit)
            }
        }
    }

}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen(
            needClear = false,
            navigateToMenu = {}
        )
    }
}