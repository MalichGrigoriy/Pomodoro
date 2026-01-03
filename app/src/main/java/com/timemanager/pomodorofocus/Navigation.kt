package com.timemanager.pomodorofocus

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.timemanager.pomodorofocus.ui.components.SharedTimerSizeState
import com.timemanager.pomodorofocus.ui.components.getSharedState
import com.timemanager.pomodorofocus.ui.settings.SettingsScreen
import com.timemanager.pomodorofocus.ui.statistic.StatisticScreen
import com.timemanager.pomodorofocus.ui.timer.TimerScreen
import com.timemanager.pomodorofocus.ui.timer.TimerViewModel
import com.timemanager.pomodorofocus.util.capitalizeString


const val TRANSITION_DURATION_MILLIS = 300

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        bottomNavigationList.forEach { bottomTab ->
            NavigationBarItem(
                selected = currentRoute == bottomTab.route,
                onClick = { navController.navigateSingleTopTo(bottomTab.route) },
                icon = { Icon(bottomTab.icon, contentDescription = bottomTab.route) },
                label = { Text(bottomTab.route.capitalizeString()) }
            )
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    timerViewModel: TimerViewModel,
    modifier: Modifier = Modifier
) {

    val sharedState: SharedTimerSizeState by
        getSharedState(navController = navController, durationMillis = TRANSITION_DURATION_MILLIS)

    NavHost(navController, startDestination = TimerDestination.route, modifier = modifier) {
        composable(
            route = TaskManagerDestination.route,
            enterTransition = { slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.End) },
            exitTransition = { slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start) }
        ) { StatisticScreen() }

        composable(
            route = TimerDestination.route,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS)) }
        ) {
            TimerScreen(
                sharedState = sharedState,
                timerViewModel = timerViewModel,
            )
        }

        composable(
            route = SettingsDestination.route,
            enterTransition = { fadeIn(animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS)) },
            exitTransition = { fadeOut(animationSpec = tween(durationMillis = TRANSITION_DURATION_MILLIS)) }
        ) {
            SettingsScreen(
                sharedState = sharedState,
                timerViewModel = timerViewModel,
                navigateToMain = { navController.navigateSingleTopTo(TimerDestination.route) }
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
