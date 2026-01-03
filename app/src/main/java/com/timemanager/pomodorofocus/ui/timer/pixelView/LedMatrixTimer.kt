package com.timemanager.pomodorofocus.ui.timer.pixelView

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.min
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme
import kotlinx.coroutines.delay

private const val stringLedCountX = 17
private const val stringLedCountY = 5
private const val minMarginX = 3
private const val minMarginY = 1
private const val ledCountX: Int = stringLedCountX + minMarginX * 2
private const val ledMinCountY = stringLedCountY + minMarginY * 2

@Composable
fun LedMatrixTimer(
    milliseconds: Int,
    ledColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    spacePercent: Float = 0.1f,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {

        val ledCountY: Int = calculateLedCountY(this.maxWidth, this.maxHeight, ledCountX)
        val image: ImageBitmap = remember { drawOvalOnBitmap(ledCountY, ledCountX) }

        val pixelSize: Dp = calculatePixelSize(maxWidth, maxHeight, image, spacePercent)
        val spacingValue = pixelSize * spacePercent

        LedMatrixBackground(
            image = image,
            ledColor = backgroundColor,
            pixelSize = pixelSize,
            spacingValue = spacingValue,
            modifier = modifier.align(Alignment.Center),
        )

        LedMatrixDigits(
            milliseconds = milliseconds,
            ledColor = ledColor,
            ledSize = pixelSize,
            spacingValue = spacingValue,
            modifier = Modifier.wrapContentSize(align = Alignment.Center)
        )
    }
}

private fun calculatePixelSize(
    width: Dp,
    height: Dp,
    image: ImageBitmap,
    spacePercent: Float,
): Dp {
    val ledSizeByWidth = width / (image.width + (image.width + 1) * spacePercent)
    val ledSizeByHeight = height / (image.height + (image.height + 1) * spacePercent)
    return min(ledSizeByWidth, ledSizeByHeight)
}

private fun calculateLedCountY(
    maxWidth: Dp,
    maxHeight: Dp,
    ledCountX: Int,
): Int {
    val ration: Float = maxWidth / maxHeight
    val ledHighTemp: Int = (ledCountX / ration).toInt()
    val ledHigh = if (ledHighTemp < ledMinCountY) {
        ledMinCountY
    } else if (ledHighTemp % 2 == 0) {
        ledHighTemp - 1
    } else {
        ledHighTemp
    }
    return ledHigh
}


@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun LedMatrixTimerPreviewDark() {
    PomodoroFocusTheme(darkTheme = true) {
        Box(modifier = Modifier.wrapContentSize()) {
            LedMatrixTimer(
                milliseconds = 523422342,
                ledColor = Color.Cyan,
                backgroundColor = Color.Red.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun LedMatrixTimerPreviewLight() {
    PomodoroFocusTheme(darkTheme = false) {

        val millisecondsState = remember { mutableIntStateOf(1923424) }
        LaunchedEffect(key1 = true) {
            while (true) {
                delay(1000L)
                millisecondsState.intValue += 1000
            }
        }

        LedMatrixTimer(
            milliseconds = millisecondsState.intValue,
            ledColor = Color.Blue,
            backgroundColor = Color.Red.copy(alpha = 0.7f),
            modifier = Modifier
        )
    }
}
