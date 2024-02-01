package com.master.sr.feature.main

import android.graphics.Bitmap

data class MainUiState(
    val startBmp: Bitmap? = null,
    val endBmp: Bitmap? = null,
    val loading: Boolean = false,
    val comparing: Boolean = true,
    val compressImageIndex: Int = 0,
    val modelBackendIndex: Int = 0
)