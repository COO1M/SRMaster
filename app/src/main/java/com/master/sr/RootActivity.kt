package com.master.sr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.master.sr.ui.AppTheme
import com.master.sr.utils.TwUtil
import com.master.sr.view.MainScreen

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        XXPermissions.with(this)
            .permission(Permission.READ_MEDIA_IMAGES)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                    if (all) {
                        setContent { AppTheme { Surface { MainScreen() } } }
                    }
                }

                override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                    if (never) {
                        TwUtil.res(R.string.permit_yourself)
                        XXPermissions.startPermissionActivity(this@RootActivity, permissions)
                    } else {
                        TwUtil.res(R.string.permit_refuse)
                    }
                }
            })
    }
}