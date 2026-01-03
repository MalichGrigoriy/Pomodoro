package com.timemanager.pomodorofocus.ui.timer.simpleView

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.delay

@Composable
fun DualTimer(
    colorText: Color,
    colorBackground: Color,
    showNegativeSign: Boolean,
    minutes: Int,
    seconds: Int,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .background(
                colorBackground,
                shape = androidx.compose.foundation.shape.CircleShape
            )
    ) {

        val multiplier = 0.40f
        val fontSize =
            with(LocalDensity.current) { (this@BoxWithConstraints.maxWidth * multiplier).toSp() }
        val textPadding = with(LocalDensity.current) { (maxWidth * multiplier * 0.86f) }
        val negativeSignPadding = with(LocalDensity.current) { (maxWidth * multiplier * 0.04f) }

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (negativeSign, minutesText, secondsText) = createRefs()

            Text(
                text = if (showNegativeSign) "-" else "",
                style = TextStyle(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colorText
                ),
                modifier = Modifier
                    .constrainAs(negativeSign) {
                        end.linkTo(minutesText.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(end = negativeSignPadding)
            )

            Text(
                text = minutes.toString().padStart(2, '0'),
                style = TextStyle(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colorText,
                    lineHeight = fontSize * 0.5f
                ),
                modifier = Modifier
                    .constrainAs(minutesText) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(bottom = textPadding)
            )

            Text(
                text = seconds.toString().padStart(2, '0'),
                style = TextStyle(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    color = colorText
                ),
                modifier = Modifier
                    .constrainAs(secondsText) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(top = textPadding)
            )
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
fun TimerDualPreview() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {

        val secondsState = remember { mutableIntStateOf(1) }
        LaunchedEffect(key1 = true) {
            while (true) {
                delay(1000L)
                secondsState.intValue += 1
            }
        }

        DualTimer(
            colorText = Color.White,
            colorBackground = Color.Black,
            showNegativeSign = true,
            minutes = 9,
            seconds = secondsState.intValue,
            modifier = Modifier.size(200.dp)
        )
    }
}
