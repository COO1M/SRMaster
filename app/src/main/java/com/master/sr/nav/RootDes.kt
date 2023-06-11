package com.master.sr.nav

sealed class RootDes(
    val route: String
) {

    object Main : RootDes("main") {
        const val needClear = "needClear"
    }

    object Menu : RootDes("menu")

}
