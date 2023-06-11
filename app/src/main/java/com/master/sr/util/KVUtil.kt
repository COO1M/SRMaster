package com.master.sr.util

import android.content.Context
import com.tencent.mmkv.MMKV

object KVUtil {

    lateinit var kv: MMKV
    private const val compressImageKey = "compress_image_index"
    private const val modelBackendKey = "model_backend_index"

    fun init(context: Context) {
        MMKV.initialize(context)
        kv = MMKV.defaultMMKV()
    }

    fun getCompressImageIndex(): Int {
        return kv.decodeInt(compressImageKey, 0)
    }

    fun setCompressImageIndex(index: Int) {
        kv.encode(compressImageKey, index)
    }

    fun getModelBackendIndex(): Int {
        return kv.decodeInt(modelBackendKey, 0)
    }

    fun setModelBackendIndex(index: Int) {
        kv.encode(modelBackendKey, index)
    }

}