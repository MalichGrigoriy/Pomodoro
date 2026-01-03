package com.timemanager.pomodorofocus.ui.timer.simpleView

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme

@Composable
fun TripleTimer(
    colorText: Color,
    colorBackground: Color,
    showNegativeSign: Boolean,
    minutes: Int,
    seconds: Int,
    milliseconds: Int,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .background(
                colorBackground,
                shape = androidx.compose.foundation.shape.CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {

        val multiplier = 0.22f
        val fontSize = with(LocalDensity.current) { (maxWidth * multiplier * 1.8f).toSp() }
        val fontSizeNegative = with(LocalDensity.current) { (maxWidth * multiplier * 1.4f).toSp() }
        val fontSizeSmall = with(LocalDensity.current) { (maxWidth * multiplier).toSp() }
        val textPaddingTop = with(LocalDensity.current) { (maxWidth * multiplier * 1.16f) }
        val textPaddingBottom = with(LocalDensity.current) { (maxWidth * multiplier * 1.66f) }
        val textPaddingBottomHorizontal =
            with(LocalDensity.current) { (maxWidth * multiplier * 0.14f) }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            val (negativeMark, minutesText, secondsText, colon2, millisecondsText) = createRefs()

            Text(
                text = minutes.toString().padStart(2, '0'),
                style = TextStyle(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colorText
                ),
                modifier = Modifier
                    .constrainAs(minutesText) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(bottom = textPaddingTop)
            )

            // Seconds
            Text(
                text = seconds.toString().padStart(2, '0'),
                style = TextStyle(
                    fontSize = fontSizeSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorText
                ),
                modifier = Modifier
                    .constrainAs(secondsText) {
                        end.linkTo(colon2.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(top = textPaddingBottom, end = textPaddingBottomHorizontal)
            )

            // colon
            Text(
                text = ":",
                style = TextStyle(
                    fontSize = fontSizeSmall * 0.8f,
                    fontWeight = FontWeight.Bold,
                    color = colorText
                ),
                modifier = Modifier
                    .constrainAs(colon2) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(top = textPaddingBottom)
            )

            // Milliseconds
            Text(
                text = milliseconds.toString().padStart(2, '0'),
                style = TextStyle(
                    fontSize = fontSizeSmall,
                    fontWeight = FontWeight.Bold,
                    color = colorText
                ),
                modifier = Modifier
                    .constrainAs(millisecondsText) {
                        start.linkTo(colon2.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(top = textPaddingBottom, start = textPaddingBottomHorizontal)
            )

            if (showNegativeSign) {
                Text(
                    text = "-",
                    style = TextStyle(
                        fontSize = fontSizeNegative,
                        fontWeight = FontWeight.Bold,
                        color = colorText
                    ),
                    modifier = Modifier
                        .constrainAs(negativeMark) {
                            top.linkTo(parent.top)
                            end.linkTo(secondsText.start)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }
        }
    }
}

@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerTriplePreview() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        TripleTimer(
            colorText = Color.White,
            colorBackground = Color.Black,
            showNegativeSign = true,
            minutes = 9,
            seconds = 5,
            milliseconds = 22,
            modifier = Modifier.size(200.dp)
        )
    }
}
