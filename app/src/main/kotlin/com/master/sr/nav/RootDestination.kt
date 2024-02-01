package com.master.sr.nav

sealed class RootDestination(
    val route: String
) {

    data object Main : RootDestination("main") {
        const val needClear = "needClear"
    }

    data object Menu : RootDestination("menu")

}
