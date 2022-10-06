package com.master.sr

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectPicture = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.select(it)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        val (progress, btn_info, btn_compare, btn_select, btn_run, btn_save) = createRefs()
        createHorizontalChain(btn_select, btn_run, btn_save, chainStyle = ChainStyle.Packed)

        //Preview Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (uiState.comparing) uiState.startBmp else uiState.endBmp)
                .crossfade(200)
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

        //About ImageButton
        ImgBtn(
            iconId = R.drawable.ic_baseline_contact_support,
            labelId = R.string.info,
            modifier = Modifier
                .constrainAs(btn_info) {
                    top.linkTo(parent.top, 50.dp)
                    end.linkTo(parent.end, 20.dp)
                },
            onClick = { viewModel.urlGo() }
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
            onClick = { viewModel.compare() }
        )

        //Select Button
        Btn(
            labelId = R.string.select,
            enabled = !uiState.loading,
            modifier = Modifier.constrainAs(btn_select) {
                bottom.linkTo(parent.bottom, 30.dp)
                start.linkTo(parent.start)
                end.linkTo(btn_run.start)
            },
            onClick = { selectPicture.launch("image/*") }
        )

        //Run Button
        Btn(
            labelId = R.string.run,
            enabled = !uiState.loading && uiState.startBmp != null,
            modifier = Modifier.constrainAs(btn_run) {
                top.linkTo(btn_select.top)
                start.linkTo(btn_select.end, 20.dp)
                end.linkTo(btn_save.start, 20.dp)
            },
            onClick = { viewModel.run() }
        )

        //Save Button
        Btn(
            labelId = R.string.save,
            enabled = !uiState.loading && uiState.startBmp != null && uiState.endBmp != null,
            modifier = Modifier.constrainAs(btn_save) {
                top.linkTo(btn_select.top)
                start.linkTo(btn_run.end)
                end.linkTo(parent.end)
            },
            onClick = { viewModel.save() }
        )

    }

}