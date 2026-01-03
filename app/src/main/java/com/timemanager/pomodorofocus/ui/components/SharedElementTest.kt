package com.timemanager.pomodorofocus.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.timemanager.pomodorofocus.SettingsDestination
import com.timemanager.pomodorofocus.TimerDestination

data class SharedTimerSizeState(
    val percentSize: Float,
    val offset: Offset
)

const val percentSizeTimer = 0.8f
const val percentSizeSettings = 0.6f

@Composable
fun getSharedState(
    navController: NavHostController,
    durationMillis: Int
): State<SharedTimerSizeState> {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var transitionState by remember { mutableStateOf(false) }

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        when (navBackStackEntry?.destination?.route) {
            TimerDestination.route -> {
                transitionState = false
            }

            SettingsDestination.route -> {
                transitionState = true
            }
        }
    }

    val animatedFloat: State<Float> = animateFloatAsState(
        targetValue = if (transitionState) percentSizeSettings else percentSizeTimer,
        animationSpec = tween(durationMillis = durationMillis),
        label = "percentSizeAnimation"
    )

    //not tested implementation
    animateOffsetAsState(
        targetValue = if (transitionState) Offset(0f, 0f) else Offset(0f, 0f),
        animationSpec = tween(durationMillis = 1000),
        label = "offsetAnimation"
    )

    val sharedState: State<SharedTimerSizeState> = remember(animatedFloat) {
        derivedStateOf {
            SharedTimerSizeState(
                animatedFloat.value,
                Offset(0f, 0f)
            ) //todo use @animatedOffset if need it
        }
    }

    return sharedState
}
