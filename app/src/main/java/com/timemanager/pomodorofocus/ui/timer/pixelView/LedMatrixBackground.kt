package com.timemanager.pomodorofocus.ui.timer.pixelView

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PixelMap
import androidx.compose.ui.graphics.toPixelMap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme

@Composable
fun LedMatrixBackground(
    image: ImageBitmap,
    ledColor: Color,
    pixelSize: Dp,
    spacingValue: Dp,
    modifier: Modifier = Modifier,
    backgroundContrast: State<Float> = mutableFloatStateOf(0.7f),
) {

    val width = pixelSize * image.width + spacingValue * (image.width + 1)
    val height = pixelSize * image.height + spacingValue * (image.height + 1)

    Canvas(modifier = modifier.size(width, height)) {
        val pixelMap: PixelMap = image.toPixelMap()

        for (y in 0 until pixelMap.height) {
            for (x in 0 until pixelMap.width) {
                var color: Color = pixelMap[x, y]
                if (color.alpha <= backgroundContrast.value) {
                    color = Color.Transparent
                } else {
                    color = ledColor
                }
                drawRect(
                    color = color,
                    topLeft = Offset(
                        x * pixelSize.toPx() + (x + 1) * spacingValue.toPx(),
                        y * pixelSize.toPx() + (y + 1) * spacingValue.toPx()
                    ),
                    size = Size(pixelSize.toPx(), pixelSize.toPx())
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
fun LedMatrixBackgroundPreviewDark() {
    PomodoroFocusTheme(darkTheme = true) {
        LedMatrixBackground(
            image = remember { drawOvalOnBitmap(20, 20) },
            ledColor = Color.Blue,
            pixelSize = 10.dp,
            spacingValue = 2.dp,
            modifier = Modifier.wrapContentSize(align = Alignment.Center),
            backgroundContrast = remember { mutableFloatStateOf(0.7f) },
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 700,
    heightDp = 1200,
)
@Composable
fun LedMatrixBackgroundPreviewLight() {
    PomodoroFocusTheme(darkTheme = false) {
            LedMatrixBackground(
                image = remember { drawOvalOnBitmap(40, 20) },
                ledColor = Color.Blue,
                pixelSize = 20.dp,
                spacingValue = 4.dp,
                modifier = Modifier.wrapContentSize(align = Alignment.Center),
                backgroundContrast = remember { mutableFloatStateOf(0.7f) },
            )
        }
}
