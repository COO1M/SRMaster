package com.master.sr

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes

object XUtil {

    fun tw(s: String) {
        Toast.makeText(App.context, s, Toast.LENGTH_SHORT).show()
    }

    fun goUrl() {
        val context = App.context
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.data = Uri.parse("https://coolm.cc/product/2")
        context.startActivity(intent)
    }

    fun stringRes(@StringRes resId: Int, vararg formatArgs: Any): String {
        return App.context.getString(resId, formatArgs)
    }

}