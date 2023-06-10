package com.master.sr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.master.sr.theme.AppTheme
import com.master.sr.util.TwUtil
import com.master.sr.view.MainScreen

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (XXPermissions.isGranted(this, Permission.READ_MEDIA_IMAGES)) {
            content()
        } else {
            XXPermissions.with(this)
                .permission(Permission.READ_MEDIA_IMAGES)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                        if (all) content()
                    }

                    override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                        if (never) {
                            TwUtil.short(R.string.permit_yourself)
                            XXPermissions.startPermissionActivity(this@RootActivity, permissions)
                        } else {
                            TwUtil.short(R.string.permit_refuse)
                        }
                    }
                })
        }
    }

    fun content() {
        setContent { AppTheme { MainScreen() } }
    }

}