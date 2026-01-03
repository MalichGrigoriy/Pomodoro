package com.timemanager.pomodorofocus.ui.timer.pixelView

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import kotlinx.coroutines.delay

@Composable
fun LedDigit(
    char: Char,
    ledColor: Color,
    ledSize: Dp,
    spacing: Dp,
    modifier: Modifier = Modifier,
    ledBackgroundColor: Color = Color.Transparent,
) {
    val block = char.toPixelBlock()

    val totalWidth = (block.width * ledSize + (block.width - 1) * spacing)
    val totalHeight = (block.height * ledSize + (block.height - 1) * spacing)

    Canvas(modifier = modifier.size(totalWidth, totalHeight)) {

        for (row in 0 until block.height) {
            for (col in 0 until block.width) {
                drawRect(
                    color = if (block.get(col, row)) ledColor else ledBackgroundColor,
                    topLeft = Offset(
                        col * (ledSize + spacing).toPx(),
                        row * (ledSize + spacing).toPx()
                    ),
                    size = Size(ledSize.toPx(), ledSize.toPx())
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun LedDigitPreviewDark() {
    LedDigit(
        char = '8',
        ledColor = Color.Blue,
        ledSize = 60.dp,
        spacing = 2.dp,
        modifier = Modifier
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun LedDigitPreviewLight() {

    val millisecondsState  = remember { mutableIntStateOf(1923424) }
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(1000L)
            millisecondsState.intValue += 1
        }
    }

    val minutesFirstDigit =  remember { derivedStateOf { (millisecondsState.intValue).toString().last() } }

    LedDigit(
        char = minutesFirstDigit.value,
        ledColor = Color.Blue,
        ledSize = 100.dp,
        spacing = 2.dp,
        modifier = Modifier
    )
}
