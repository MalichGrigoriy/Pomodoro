package com.timemanager.pomodorofocus.ui.statistic

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.timemanager.pomodorofocus.domain.model.StatisticListItem
import com.timemanager.pomodorofocus.domain.model.Task
import com.timemanager.pomodorofocus.domain.model.TimerBase
import com.timemanager.pomodorofocus.domain.settings.DEFAULT_TIMER_DURATION
import com.timemanager.pomodorofocus.domain.statistic.toStatisticFinishedItem
import com.timemanager.pomodorofocus.ui.theme.PomodoroFocusTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

@Composable
fun StatisticScreen(
    statisticViewModel: StatisticViewModel = hiltViewModel<StatisticViewModel>()
) {
    val timerGroups: LazyPagingItems<StatisticListItem> =
        statisticViewModel.taskGroups.collectAsLazyPagingItems()

    StatisticList(timerGroups)
}

@Composable
private fun StatisticList(timerGroups: LazyPagingItems<StatisticListItem>) { //todo check unstable params
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(timerGroups.itemCount) { index ->
            when (val item = timerGroups[index]) {
                is StatisticListItem.Header -> HeaderCard(item)

                is StatisticListItem.StatisticFinishedItem -> StatisticCard(
                    timer = item,
                    afterHeaderModifier(timerGroups[index - 1] is StatisticListItem.Header)
                )

                else -> {}
            }
        }
    }
}

@Composable
private fun afterHeaderModifier(isFirstAfterHeader: Boolean): Modifier {
    var modifier = Modifier
        .padding(horizontal = 16.dp)
    if (isFirstAfterHeader) {
        modifier = modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    Color.Transparent
                )
            )
        )
    }
    return modifier
}

@Composable
private fun HeaderCard(item: StatisticListItem.Header) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    )
                ),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                )
            )

    ) {
        Column {
            Text(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.86f),
                text = item.date,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.16f),
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

val smallTimerPreview = TimerBase.Completed(
    id = UUID.randomUUID(),
    createdAt = 0,
    startTime = 400_000L,
    expectedDuration = DEFAULT_TIMER_DURATION,
    actualEndTime = DEFAULT_TIMER_DURATION,
    task = Task(
        name = " ",
    )
)

val normalTimerPreview = smallTimerPreview.copy(
    startTime = 0,
)

val list: Flow<PagingData<StatisticListItem>>
    @Composable
    get() = remember {
        MutableStateFlow(
            PagingData.from(
                listOf(
                    StatisticListItem.Header("Today, 07 May 2021"),
                    smallTimerPreview.toStatisticFinishedItem(),
                    normalTimerPreview.toStatisticFinishedItem()
                )
            )
        )
    }

@Preview(
    name = "Light Theme Preview",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 360,
    heightDp = 300,
)

@Preview(
    name = "Dark Theme Preview",
    showBackground = true,
    backgroundColor = 0x333333,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 360,
    heightDp = 300,
)
@Composable
private fun PreviewStatisticList() {
    val isDarkTheme =
        LocalConfiguration.current.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    PomodoroFocusTheme(darkTheme = isDarkTheme) {
        StatisticList(list.collectAsLazyPagingItems())
    }
}
