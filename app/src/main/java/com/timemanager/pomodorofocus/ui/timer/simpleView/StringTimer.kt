package com.timemanager.pomodorofocus.ui.timer.simpleView

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme
import java.util.Locale

@Composable
fun StringTimer(
    colorText: Color,
    colorBackground: Color,
    showNegativeSign: Boolean,
    minutes: Int,
    seconds: Int,
    modifier: Modifier = Modifier,
    milliseconds: Int? = null,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = modifier
            .background(
                colorBackground,
                shape = androidx.compose.foundation.shape.CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        val text = buildString {
            if (showNegativeSign) append("-")  // Add "-" if needed
            append("%02d:%02d".format(Locale.US, minutes, seconds))  // MM:SS
            milliseconds?.let { milliseconds ->
                append(":%02d".format(Locale.US, milliseconds))
            }
        }

        var optimalFontSize: TextUnit = TextUnit.Unspecified

        for (fontSize in 4..200 step 2) {
            val measuredText = textMeasurer.measure(
                text = text,
                style = TextStyle(fontSize = fontSize.sp),
                maxLines = 1,
            )

            if (measuredText.size.width > with(density) { this@BoxWithConstraints.maxWidth.toPx() * 0.8 }) break
            optimalFontSize = fontSize.sp
        }

        Text(
            text = text,
            style = TextStyle(
                fontSize = optimalFontSize,
                fontWeight = FontWeight.Bold,
                color = colorText
            ),
            maxLines = 1,
        )
    }
}

@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerStringPreview() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        StringTimer(
            colorText = Color.White,
            colorBackground = Color.Black,
            showNegativeSign = true,
            minutes = 9,
            seconds = 5,
            milliseconds = 0,
            modifier = Modifier.size(200.dp)
        )
    }
}
