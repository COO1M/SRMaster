package com.master.sr.utils

import android.widget.Toast
import androidx.annotation.StringRes
import com.master.sr.app.App

object XUtil {

    fun tw(s: String) {
        Toast.makeText(App.context, s, Toast.LENGTH_SHORT).show()
    }

    fun stringRes(@StringRes resId: Int, vararg formatArgs: Any): String {
        return App.context.getString(resId, formatArgs)
    }

}