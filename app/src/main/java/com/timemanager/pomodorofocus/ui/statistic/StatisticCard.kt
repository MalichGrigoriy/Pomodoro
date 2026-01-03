package com.timemanager.pomodorofocus.ui.statistic

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.timemanager.pomodorofocus.domain.model.StatisticListItem
import com.timemanager.pomodorofocus.domain.statistic.toStatisticFinishedItem
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme

@Composable
fun StatisticCard(
    timer: StatisticListItem.StatisticFinishedItem,
    modifier: Modifier  = Modifier
) {

    val colorText = when (timer.percent) {
        in 0..75 -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val (progress, taskName, taskDescription, duration) = createRefs()

        IconPercent(
            timer.percent,
            colorText,
            Modifier
                .padding(horizontal = 4.dp)
                .constrainAs(progress) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        Text(
            text = "${timer.duration.toMinutes()} min - ",
            fontSize = 16.sp,
            color = colorText,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .constrainAs(duration) {
                    start.linkTo(progress.end)
                    baseline.linkTo(taskName.baseline)
                }
        )

        Text(
            text = timer.taskTitle,
            fontSize = 16.sp,
            color = colorText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(top = 4.dp)
                .constrainAs(taskName) {
                    start.linkTo(duration.end)
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = timer.taskDescription,
            fontSize = 14.sp,
            color = colorText,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .constrainAs(taskDescription) {
                    start.linkTo(progress.end)
                    top.linkTo(taskName.bottom)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

private fun Long.toMinutes(): String = (this / 60000).toString().padStart(2, '0')

@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)

@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x333333,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun PreviewStatisticList() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        Column {
            StatisticCard(smallTimerPreview.toStatisticFinishedItem())
            StatisticCard(normalTimerPreview.toStatisticFinishedItem())
        }
    }
}
