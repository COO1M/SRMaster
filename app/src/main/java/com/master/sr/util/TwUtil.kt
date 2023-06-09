package com.master.sr.util

import android.widget.Toast
import com.master.sr.app.App

object TwUtil {

    private lateinit var lastToast: Toast

    fun short(s: String) {
        toast(s, Toast.LENGTH_SHORT)
    }

    fun short(id: Int, vararg args: Any) {
        toast(App.ctx.getString(id, *args), Toast.LENGTH_SHORT)
    }

    fun long(id: Int, vararg args: Any) {
        toast(App.ctx.getString(id, *args), Toast.LENGTH_LONG)
    }

    private fun toast(s: String, duration: Int) {
        App.act.runOnUiThread {
            if (this::lastToast.isInitialized) lastToast.cancel()
            lastToast = Toast.makeText(App.ctx, s, duration)
            lastToast.show()
        }
    }

}