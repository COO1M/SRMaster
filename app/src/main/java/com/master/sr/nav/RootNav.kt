package com.master.sr.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        enterTransition = {
            fadeIn(tween(300)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = tween(300),
                initialOffset = { it / 2 }
            )
        },
        exitTransition = { fadeOut(tween(300)) },
        popEnterTransition = { fadeIn(tween(300)) },
        popExitTransition = {
            fadeOut(tween(300)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = tween(300),
                targetOffset = { it / 2 })
        }
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