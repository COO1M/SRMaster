package com.master.sr.utils

import android.widget.Toast
import com.master.sr.app.App

object TwUtil {

    fun str(s: String) {
        Toast.makeText(App.ctx, s, Toast.LENGTH_SHORT).show()
    }

    fun res(id: Int, vararg args: Any) {
        Toast.makeText(App.ctx, App.ctx.getString(id, args), Toast.LENGTH_SHORT).show()
    }

}