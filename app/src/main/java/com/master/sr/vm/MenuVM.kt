package com.master.sr.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.master.sr.R
import com.master.sr.app.App
import com.master.sr.util.KVUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuVM : ViewModel() {

    private var _uiState = MutableStateFlow(MenuUiState())
    val uiState = _uiState.asStateFlow()

    val compressImageItems: Array<String> =
        App.ctx.resources.getStringArray(R.array.compress_image_choice)
    val modelBackendItems: Array<String> =
        App.ctx.resources.getStringArray(R.array.model_backend_choice)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    initialCompressImageIndex = KVUtil.getCompressImageIndex(),
                    compressImageIndex = KVUtil.getCompressImageIndex(),
                    modelBackendIndex = KVUtil.getModelBackendIndex()
                )
            }
        }
    }

    fun changeCompressImageIndex(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            KVUtil.setCompressImageIndex(index)
            _uiState.update { it.copy(compressImageIndex = index) }
        }
    }

    fun changeModelBackendIndex(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            KVUtil.setModelBackendIndex(index)
            _uiState.update { it.copy(modelBackendIndex = index) }
        }
    }

}

data class MenuUiState(
    val initialCompressImageIndex: Int = 0,
    val compressImageIndex: Int = 0,
    val modelBackendIndex: Int = 0,
)