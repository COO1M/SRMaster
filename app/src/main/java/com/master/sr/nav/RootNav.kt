package com.master.sr.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.master.sr.view.MainSC
import com.master.sr.view.MenuSC

@Composable
fun RootNav() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = RootDes.Main.route,
    ) {

        composable(route = RootDes.Main.route) { backStack ->
            val needClear = backStack.savedStateHandle.get<Boolean>(RootDes.Main.needClear) ?: false
            MainSC(
                needClear = needClear,
                navigateToMenu = { rootNavController.navigate(RootDes.Menu.route) }
            )
        }

        composable(route = RootDes.Menu.route) { backStack ->
            val previousBackStack = rootNavController.previousBackStackEntry
            MenuSC(
                navigateBack = {
                    previousBackStack?.savedStateHandle?.set(RootDes.Main.needClear, it)
                    rootNavController.popBackStack()
                }
            )
        }

    }
}