package com.master.sr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.master.sr.nav.RootNav
import com.master.sr.theme.AppTheme
import com.master.sr.util.TwUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splash = installSplashScreen().apply { setKeepOnScreenCondition { true } }
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

        lifecycleScope.launch {
            delay(300)
            splash.setKeepOnScreenCondition { false }
        }
    }

    fun content() {
        setContent {
            AppTheme {
                Surface {
                    RootNav()
                }
            }
        }
    }

}