package com.timemanager.pomodorofocus.ui.statistic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.timemanager.pomodorofocus.R
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme


@Composable
fun IconPercent(
    percent: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
    ) {
        if (percent == 100) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_check_circle_outline_24),
                contentDescription = "OK Icon",
                tint = color,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            )
        } else {
            CircularProgressIndicator(
                progress = {percent / 100f},
                color = color,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)

            )

            CircularProgressIndicator(
                progress = {1f},
                color = color.copy(alpha = 0.1f),
                strokeWidth = 4.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )

            Text(
                text = "$percent%",
                fontSize = 12.sp,
                color = color,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewPercentIcon() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        IconPercent(
            percent = 33,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}
