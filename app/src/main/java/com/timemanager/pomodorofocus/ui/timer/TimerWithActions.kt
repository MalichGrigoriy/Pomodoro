package com.timemanager.pomodorofocus.ui.timer

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.timemanager.pomodorofocus.R
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme
import com.timemanager.pomodorofocus.ui.timer.pixelView.LedMatrixTimer
import com.timemanager.pomodorofocus.ui.timer.simpleView.DualTimer
import com.timemanager.pomodorofocus.ui.timer.simpleView.EmptyTimer
import com.timemanager.pomodorofocus.ui.timer.simpleView.StringTimer
import com.timemanager.pomodorofocus.ui.timer.simpleView.TimerType
import com.timemanager.pomodorofocus.ui.timer.simpleView.TripleTimer

@Composable
fun TimerWithActions(
    timerState: TimerStateHolder,
    modifier: Modifier = Modifier,
) {
    val colorBackground =
        if (timerState.isNegativeColor) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
    val colorText =
        if (timerState.isNegativeColor) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer

    ConstraintLayout(
        modifier,
    ) {
        val image = createRef()

        val guidelineValues = getGuideline(timerState.selectedTimerType)
        val guidePointBottom = createGuidelineFromBottom(guidelineValues.bottomGuide)
        val guidePointEnd = createGuidelineFromEnd(guidelineValues.endGuide)

        if (timerState.action == TimerAction.EMPTY) {
            EmptyTimer(
                colorBackground = colorBackground,
                modifier = Modifier.fillMaxSize()
            )
        } else {

            when (timerState.selectedTimerType) {
                TimerType.String -> StringTimer(
                    colorText = colorText,
                    colorBackground = colorBackground,
                    showNegativeSign = timerState.isNegativeMark,
                    minutes = timerState.minutes,
                    seconds = timerState.seconds,
                    milliseconds = null,
                    modifier = Modifier.fillMaxSize()
                )

                TimerType.StringLong -> StringTimer(
                    colorText = colorText,
                    colorBackground = colorBackground,
                    showNegativeSign = timerState.isNegativeMark,
                    minutes = timerState.minutes,
                    seconds = timerState.seconds,
                    milliseconds = timerState.milliseconds,
                    modifier = Modifier.fillMaxSize()
                )

                TimerType.Dual -> DualTimer(
                    colorText = colorText,
                    colorBackground = colorBackground,
                    showNegativeSign = timerState.isNegativeMark,
                    minutes = timerState.minutes,
                    seconds = timerState.seconds,
                    modifier = Modifier.fillMaxSize()
                )

                TimerType.Triple -> TripleTimer(
                    colorText = colorText,
                    colorBackground = colorBackground,
                    showNegativeSign = timerState.isNegativeMark,
                    minutes = timerState.minutes,
                    seconds = timerState.seconds,
                    milliseconds = timerState.milliseconds,
                    modifier = Modifier.fillMaxSize()
                )

                TimerType.LedMatrix -> LedMatrixTimer(
                    ledColor = colorText,
                    backgroundColor = colorBackground,
                    milliseconds = timerState.millisecondsFull,
                    modifier = Modifier.fillMaxSize()
                )

                TimerType.Empty -> EmptyTimer(
                    colorBackground = colorBackground,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        if (timerState.action != TimerAction.EMPTY) {
            Crossfade(
                targetState = timerState.action,
                modifier = Modifier
                    .fillMaxSize(0.3f)
                    .constrainAs(image) {
                        centerAround(guidePointBottom)
                        centerAround(guidePointEnd)
                    }
            ) { action: TimerAction ->
                val (iconPainter, iconPadding) = getIcon(action)

                Icon(
                    painter = painterResource(iconPainter),
                    tint = colorText.copy(alpha = 0.2f),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(iconPadding)
                )
            }
        }
    }
}

private data class IconData(@param:DrawableRes val drawableResId: Int, val padding: Dp)

private fun getIcon(action: TimerAction): IconData {
    return when (action) {
        TimerAction.START -> IconData(R.drawable.baseline_play_arrow_24, 0.dp)
        TimerAction.STOP -> IconData(R.drawable.baseline_pause_24, 4.dp)
        TimerAction.RESTART -> IconData(R.drawable.baseline_refresh_24, 8.dp)
        else -> IconData(R.drawable.ic_launcher_foreground, 0.dp)
    }
}

private data class GuidelineFloatValues(val bottomGuide: Float, val endGuide: Float)

private fun getGuideline(timerVariant: TimerType): GuidelineFloatValues {
    return when (timerVariant) {
        TimerType.LedMatrix,
        TimerType.String,
        TimerType.StringLong -> GuidelineFloatValues(bottomGuide = 0.2f, endGuide = 0.5f)

        TimerType.Dual -> GuidelineFloatValues(bottomGuide = 0.5f, endGuide = 0.14f)

        TimerType.Triple -> GuidelineFloatValues(bottomGuide = 0.14f, endGuide = 0.5f)

        TimerType.Empty -> GuidelineFloatValues(bottomGuide = 0.5f, endGuide = 0.5f)
    }
}


@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    backgroundColor = 0xFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerBoxPreviewNew() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        TimerWithActions(
            TimerStateHolder(
                minutes = 12,
                seconds = 34,
                milliseconds = 0,
                millisecondsFull = 1239834090,
                action = TimerAction.START,
                isNegativeMark = false,
                isNegativeColor = false,
                selectedTimerType = TimerType.String,
            ),
            Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    backgroundColor = 0xFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerBoxPreviewProgress() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        TimerWithActions(
            TimerStateHolder(
                minutes = 12,
                seconds = 34,
                milliseconds = 0,
                millisecondsFull = 1239834090,
                action = TimerAction.STOP,
                isNegativeMark = false,
                isNegativeColor = false,
                selectedTimerType = TimerType.Dual,
            ),
            Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    backgroundColor = 0xFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerBoxPreviewRestart() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        TimerWithActions(
            TimerStateHolder(
                minutes = 12,
                seconds = 34,
                milliseconds = 0,
                millisecondsFull = 1239834090,
                action = TimerAction.RESTART,
                isNegativeMark = false,
                isNegativeColor = false,
                selectedTimerType = TimerType.LedMatrix,
            ),
            Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    backgroundColor = 0xFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerBoxPreviewMilliseconds() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        TimerWithActions(
            TimerStateHolder(
                minutes = 12,
                seconds = 34,
                milliseconds = 39,
                millisecondsFull = 1239834090,
                action = TimerAction.RESTART,
                isNegativeMark = false,
                isNegativeColor = false,
                selectedTimerType = TimerType.Triple,
            ),
            Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    backgroundColor = 0xFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerBoxPreviewNegative() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        TimerWithActions(
            TimerStateHolder(
                minutes = 12,
                seconds = 34,
                milliseconds = 39,
                millisecondsFull = 1239834090,
                action = TimerAction.RESTART,
                isNegativeMark = true,
                isNegativeColor = false,
                selectedTimerType = TimerType.String,
            ),
            Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    backgroundColor = 0xFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerBoxPreviewNegativeColor() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        TimerWithActions(
            TimerStateHolder(
                minutes = 12,
                seconds = 34,
                milliseconds = 39,
                millisecondsFull = 1239834090,
                action = TimerAction.RESTART,
                isNegativeMark = false,
                isNegativeColor = true,
                selectedTimerType = TimerType.String,
            ),
            Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.background)
        )
    }
}
