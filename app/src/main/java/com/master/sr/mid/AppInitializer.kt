package com.master.sr.mid

import android.content.Context
import androidx.startup.Initializer
import com.master.sr.app.Constant
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
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}