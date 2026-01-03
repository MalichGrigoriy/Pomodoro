package com.timemanager.pomodorofocus.ui.timer.pixelView

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.timemanager.pomodorofocus.util.onlyMinutes
import com.timemanager.pomodorofocus.util.onlySeconds
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LedMatrixDigits(
    milliseconds: Int,
    ledColor: Color,
    ledSize: Dp,
    spacingValue: Dp,
    modifier: Modifier = Modifier,
    spacingPercentage: Float = 0.1f,
) {
    val minutes: Long =  milliseconds.milliseconds.onlyMinutes
    val seconds: Long = milliseconds.milliseconds.onlySeconds

    val minutesFirstDigit: Char = (minutes / 10).toString().first()
    val minutesSecondDigit: Char = (minutes % 10).toString().first()
    val secondsFirst: Char = (seconds / 10).toString().first()
    val secondsSecond: Char = (seconds % 10).toString().first()

    Row(
        modifier = modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(spacingValue * 2 + ledSize)
    ) {

        LedDigit(
            char = minutesFirstDigit,
            ledColor = ledColor,
            ledSize = ledSize,
            spacing = spacingValue,
            modifier = Modifier
        )

        LedDigit(
            char = minutesSecondDigit,
            ledColor = ledColor,
            ledSize = ledSize,
            spacing = spacingValue,
            modifier = Modifier
        )

        LedDigit(
            char = ':',
            ledColor = ledColor,
            ledSize = ledSize,
            spacing = spacingValue,
            modifier = Modifier
        )

        LedDigit(
            char = secondsFirst,
            ledColor = ledColor,
            ledSize = ledSize,
            spacing = spacingValue,
            modifier = Modifier
        )

        LedDigit(
            char = secondsSecond,
            ledColor = ledColor,
            ledSize = ledSize,
            spacing = spacingValue,
            modifier = Modifier
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LedMatrixDigitsPreviewDark() {

    val milliseconds =  remember { mutableIntStateOf(334000) }

    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000L)
            milliseconds.intValue += 1000
        }
    }

    PomodoroFocusTheme(darkTheme = true) {
        LedMatrixDigits(
            milliseconds = milliseconds.intValue, // 5 minutes and 34 seconds in milliseconds
            ledColor = Color.Cyan,
            ledSize = 10.dp,
            spacingValue = 1.dp,
            modifier = Modifier,
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun LedMatrixDigitsPreviewLight() {
    PomodoroFocusTheme(darkTheme = false) {
        LedMatrixDigits(
            milliseconds = 11737000,// 19 minutes and 37 seconds in milliseconds
            ledColor = Color.Blue,
            ledSize = 20.dp,
            spacingValue = 2.dp,
            modifier = Modifier,
        )
    }
}
