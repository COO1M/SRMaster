package com.master.sr.vm

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.master.sr.R
import com.master.sr.util.FileUtil
import com.master.sr.util.TorchUtil
import com.master.sr.util.TwUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    //SuperMode ImageButton
    fun superMode() {
        _uiState.update {
            it.copy(startBmp = null, endBmp = null, supering = !_uiState.value.supering)
        }
        TwUtil.short(if (_uiState.value.supering) R.string.super_mode_on else R.string.super_mode_off)
    }

    //Compare ImageButton
    fun compare() {
        _uiState.update {
            it.copy(comparing = !_uiState.value.comparing)
        }
        TwUtil.short(if (_uiState.value.comparing) R.string.compare_on else R.string.compare_off)
    }

    //Select Button
    fun select(uri: Uri) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { it.copy(loading = true, startBmp = null, endBmp = null) }

        kotlin.runCatching {
            _uiState.update {
                it.copy(startBmp = FileUtil.uri2bitmap(uri, !_uiState.value.supering))
            }
        }.onFailure { t ->
            Log.e("TAG", t.stackTraceToString())
            TwUtil.long(R.string.select_fail, "${t.message}")
        }.onSuccess {
            TwUtil.short(R.string.select_success)
        }

        _uiState.update { it.copy(loading = false, comparing = true) }
    }

    //Run Button
    fun run() = viewModelScope.launch(Dispatchers.Default) {
        _uiState.update { it.copy(loading = true, endBmp = null) }

        if (_uiState.value.startBmp != null) {
            kotlin.runCatching {
                _uiState.update {
                    it.copy(endBmp = TorchUtil.runRealesrgan(_uiState.value.startBmp!!))
                }
            }.onFailure { t ->
                Log.e("TAG", t.stackTraceToString())
                TwUtil.short(R.string.run_fail, "${t.message}")
            }.onSuccess {
                TwUtil.short(R.string.run_success)
            }
        } else {
            TwUtil.short(R.string.no_input)
        }

        _uiState.update { it.copy(loading = false, comparing = false) }
    }

    //Save Button
    fun save() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { it.copy(loading = true) }

        if (_uiState.value.endBmp != null) {
            kotlin.runCatching {
                FileUtil.saveBitmap(_uiState.value.endBmp!!)
            }.onFailure { t ->
                Log.e("TAG", t.stackTraceToString())
                TwUtil.short(R.string.save_fail, "${t.message}")
            }.onSuccess {
                TwUtil.short(R.string.save_success)
            }
        } else {
            TwUtil.short(R.string.no_input)
        }

        _uiState.update { it.copy(loading = false) }
    }

}

data class MainUiState(
    val startBmp: Bitmap? = null,
    val endBmp: Bitmap? = null,
    val loading: Boolean = false,
    val comparing: Boolean = true,
    val supering: Boolean = false
)