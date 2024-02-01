package com.master.sr.app

import android.content.Context
import androidx.startup.Initializer
import com.master.sr.util.MMKVUtil
import com.umeng.commonsdk.UMConfigure

class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        UMConfigure.setLogEnabled(false)
        UMConfigure.init(
            context,
            Constant.UMKey,
            Constant.UMChannel,
            UMConfigure.DEVICE_TYPE_PHONE,
            null
        )
        MMKVUtil.init(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}