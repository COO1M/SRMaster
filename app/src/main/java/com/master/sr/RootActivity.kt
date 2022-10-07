package com.master.sr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.core.view.WindowCompat
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.master.sr.ui.AppTheme
import com.master.sr.utils.XUtil
import com.master.sr.view.MainScreen

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        XXPermissions.with(this)
            .permission(Permission.READ_MEDIA_IMAGES, Permission.READ_PHONE_STATE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        setContent { AppTheme { Surface { MainScreen() } } }
                    } else {
                        XUtil.tw(XUtil.stringRes(R.string.please_permit_yourself))
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    if (never) {
                        XXPermissions.startPermissionActivity(this@RootActivity, permissions)
                    }
                }
            })
    }
}