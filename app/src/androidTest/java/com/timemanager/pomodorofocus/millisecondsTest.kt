package com.timemanager.pomodorofocus

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class millisecondsTest {


    @Preview(
        showBackground = true,
        backgroundColor = 0xFFFFFFFF,
        uiMode = Configuration.UI_MODE_NIGHT_NO
    )
    @Composable
    fun testMillisecondsToString() {

        var remainingMs: Long = 5025678L

        val hours = remainingMs / 3_600_000L // 3600 seconds in an hour * 1000 milliseconds
        remainingMs %= 3_600_000L
        val minutes = remainingMs / 60_000L // 60 seconds in a minute * 1000 milliseconds
        remainingMs %= 60_000L
        val seconds = remainingMs / 1000L // 1000 milliseconds in a second
        remainingMs %= 1000L

        val hoursTens = hours / 10
        val hoursUnits = hours % 10

        val minutesTens = minutes / 10
        val minutesUnits = minutes % 10

        val secondsTens = seconds / 10
        val secondsUnits = seconds % 10

        val millisecondsTens = remainingMs / 100
        val millisecondsUnits = remainingMs % 100 / 10


        PomodoroFocusTheme(darkTheme = false) {
            Column(modifier = Modifier) {

                Text(text = hoursTens.toString(), modifier = Modifier)
                Text(text = hoursUnits.toString(), modifier = Modifier)
                Text(text = minutesTens.toString(), modifier = Modifier)
                Text(text = minutesUnits.toString(), modifier = Modifier)
                Text(text = secondsTens.toString(), modifier = Modifier)
                Text(text = secondsUnits.toString(), modifier = Modifier)
                Text(text = millisecondsTens.toString(), modifier = Modifier)
                Text(text = millisecondsUnits.toString(), modifier = Modifier)
            }
        }
    }
}
