package com.master.sr.feature.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.master.sr.R
import com.master.sr.app.App
import com.master.sr.util.MMKVUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {

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
                    initialCompressImageIndex = MMKVUtil.getCompressImageIndex(),
                    compressImageIndex = MMKVUtil.getCompressImageIndex(),
                    modelBackendIndex = MMKVUtil.getModelBackendIndex()
                )
            }
        }
    }

    fun changeCompressImageIndex(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            MMKVUtil.setCompressImageIndex(index)
            _uiState.update { it.copy(compressImageIndex = index) }
        }
    }

    fun changeModelBackendIndex(index: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            MMKVUtil.setModelBackendIndex(index)
            _uiState.update { it.copy(modelBackendIndex = index) }
        }
    }

}