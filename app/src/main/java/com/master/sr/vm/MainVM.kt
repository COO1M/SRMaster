package com.master.sr.vm

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.master.sr.R
import com.master.sr.util.FileUtil
import com.master.sr.util.KVUtil
import com.master.sr.util.OnnxUtil
import com.master.sr.util.TorchUtil
import com.master.sr.util.TwUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainVM : ViewModel() {

    private var _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun clear() {
        _uiState.update { it.copy(startBmp = null, endBmp = null, comparing = true) }
    }

    fun initSetting() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    compressImageIndex = KVUtil.getCompressImageIndex(),
                    modelBackendIndex = KVUtil.getModelBackendIndex()
                )
            }
        }
    }

    fun compare() {
        _uiState.update { it.copy(comparing = !_uiState.value.comparing) }
        TwUtil.short(if (_uiState.value.comparing) R.string.compare_on else R.string.compare_off)
    }

    fun select(uri: Uri) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { it.copy(loading = true, startBmp = null, endBmp = null) }

        kotlin.runCatching {
            _uiState.update {
                it.copy(startBmp = FileUtil.uri2bitmap(uri, _uiState.value.compressImageIndex == 0))
            }
        }.onFailure { t ->
            TwUtil.long(R.string.select_fail, "${t.message}")
        }.onSuccess {
            TwUtil.short(R.string.select_success)
        }

        _uiState.update { it.copy(loading = false, comparing = true) }
    }

    fun run() = viewModelScope.launch(Dispatchers.Default) {
        _uiState.update { it.copy(loading = true, endBmp = null) }

        if (_uiState.value.startBmp != null) {
            kotlin.runCatching {
                _uiState.update {
                    it.copy(
                        endBmp =
                        if (_uiState.value.modelBackendIndex == 0)
                            TorchUtil.runRealesrgan(_uiState.value.startBmp!!)
                        else
                            OnnxUtil.runRealesrgan(_uiState.value.startBmp!!)
                    )
                }
            }.onFailure { t ->
                TwUtil.short(R.string.run_fail, "${t.message}")
            }.onSuccess {
                TwUtil.short(R.string.run_success)
            }
        } else {
            TwUtil.short(R.string.no_input)
        }

        _uiState.update { it.copy(loading = false, comparing = false) }
    }

    fun save() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.update { it.copy(loading = true) }

        if (_uiState.value.endBmp != null) {
            kotlin.runCatching {
                FileUtil.saveBitmap(_uiState.value.endBmp!!)
            }.onFailure { t ->
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
    val compressImageIndex: Int = 0,
    val modelBackendIndex: Int = 0
)