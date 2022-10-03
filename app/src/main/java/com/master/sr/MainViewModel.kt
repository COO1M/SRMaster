package com.master.sr

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.master.sr.utils.FileUtil
import com.master.sr.utils.TorchUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    //About ImageButton Event
    fun urlGo() {
        kotlin.runCatching {
            XUtil.goUrl()
        }.onFailure {
            XUtil.tw(XUtil.stringRes(R.string.no_app))
        }
    }

    //Compare ImageButton Event
    fun compare() {
        _uiState.update {
            it.copy(comparing = !_uiState.value.comparing)
        }
    }

    //Select Button Callback Event
    fun select(uri: Uri?) = viewModelScope.launch {
        if (uri != null) {
            kotlin.runCatching {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiState.update { it.copy(loading = true, startBmp = null, endBmp = null) }
                    _uiState.update { it.copy(startBmp = FileUtil.uri2bitmap(uri)) }
                    _uiState.update { it.copy(loading = false, comparing = true) }
                }
            }.onFailure {
                XUtil.tw(XUtil.stringRes(R.string.select_fail, it.message.toString()))
            }.onSuccess {
                XUtil.tw(XUtil.stringRes(R.string.select_success))
            }
        } else {
            XUtil.tw(XUtil.stringRes(R.string.no_select))
        }
    }

    //Run Button Event
    fun run() = viewModelScope.launch {
        if (_uiState.value.startBmp != null) {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    _uiState.update { it.copy(loading = true, endBmp = null) }
                    _uiState.update { it.copy(endBmp = TorchUtil.runRealesr(_uiState.value.startBmp!!)) }
                    _uiState.update { it.copy(loading = false, comparing = false) }
                }
            }.onFailure {
                XUtil.tw(XUtil.stringRes(R.string.run_fail, it.message.toString()))
            }.onSuccess {
                XUtil.tw(XUtil.stringRes(R.string.run_success))
            }
        } else {
            XUtil.tw(XUtil.stringRes(R.string.no_input))
        }
    }

    fun save() = viewModelScope.launch {
        if (_uiState.value.endBmp != null) {
            kotlin.runCatching {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiState.update { it.copy(loading = true) }
                    FileUtil.saveBitmap(_uiState.value.endBmp!!)
                    _uiState.update { it.copy(loading = false, comparing = false) }
                }
            }.onFailure {
                XUtil.tw(XUtil.stringRes(R.string.save_fail, it.message.toString()))
            }.onSuccess {
                XUtil.tw(XUtil.stringRes(R.string.save_success))
            }
        } else {
            XUtil.tw(XUtil.stringRes(R.string.no_input))
        }
    }

}

data class MainUiState(
    val startBmp: Bitmap? = null,
    val endBmp: Bitmap? = null,
    val loading: Boolean = false,
    val comparing: Boolean = true
)