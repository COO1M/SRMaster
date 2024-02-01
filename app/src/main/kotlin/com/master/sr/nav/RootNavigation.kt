package com.master.sr.nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.master.sr.feature.main.MainScreen
import com.master.sr.feature.menu.MenuScreen

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = RootDestination.Main.route,
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

        composable(route = RootDestination.Main.route) { backStack ->
            val needClear =
                backStack.savedStateHandle.get<Boolean>(RootDestination.Main.needClear) ?: false
            MainScreen(
                needClear = needClear,
                navigateToMenu = { rootNavController.navigate(RootDestination.Menu.route) }
            )
        }

        composable(route = RootDestination.Menu.route) {
            val previousBackStack = rootNavController.previousBackStackEntry
            MenuScreen(
                navigateBack = {
                    previousBackStack?.savedStateHandle?.set(RootDestination.Main.needClear, it)
                    rootNavController.popBackStack()
                }
            )
        }

    }
}