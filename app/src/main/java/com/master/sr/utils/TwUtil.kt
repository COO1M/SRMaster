package com.master.sr.utils

import android.widget.Toast
import com.master.sr.app.App

object TwUtil {

    fun str(s: String) {
        Toast.makeText(App.context, s, Toast.LENGTH_SHORT).show()
    }

    fun res(id: Int, vararg args: Any) {
        Toast.makeText(App.context, App.context.getString(id, args), Toast.LENGTH_SHORT).show()
    }

}