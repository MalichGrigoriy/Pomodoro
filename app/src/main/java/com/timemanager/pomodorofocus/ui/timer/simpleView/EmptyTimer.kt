package com.timemanager.pomodorofocus.ui.timer.simpleView

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme

@Composable
fun EmptyTimer(
    colorBackground: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(
                colorBackground,
                shape = androidx.compose.foundation.shape.CircleShape
            ),
    )
}

@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TimerEmptyPreview() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        EmptyTimer(
            colorBackground = Color.Black,
            modifier = Modifier.size(200.dp)
        )
    }
}
